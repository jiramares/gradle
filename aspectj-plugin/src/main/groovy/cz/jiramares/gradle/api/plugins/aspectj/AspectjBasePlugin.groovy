package cz.jiramares.gradle.api.plugins.aspectj

import org.gradle.api.Action
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileTreeElement
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.compile.Compile
import org.gradle.plugins.eclipse.EclipseClasspath
import org.gradle.api.internal.ClassGenerator

import cz.jiramares.gradle.api.internal.tasks.DefaultAspectjSourceSet
import cz.jiramares.gradle.api.tasks.AspectjSourceSet
import cz.jiramares.gradle.api.tasks.aspectj.AspectjCompile

class AspectjBasePlugin implements Plugin {
   public static final ASPECTJASPECTS_CONFIGURATION_NAME = "aspects";
   public static final ASPECTJTOOLS_CONFIGURATION_NAME = "aspectjTools";
   
   public void apply(project) {
      project.plugins.apply(JavaBasePlugin.class)
      project.configurations.add(ASPECTJASPECTS_CONFIGURATION_NAME).setVisible(false).setTransitive(false);
      project.configurations.add(ASPECTJTOOLS_CONFIGURATION_NAME).setVisible(false).setTransitive(false);
      
      configureCompileDefaults(project);
      configureSourceSetDefaults(project, project.getPlugins().apply(JavaBasePlugin.class))
   }
   
   protected void configureCompileDefaults(Project project) {
      project.tasks.withType(AspectjCompile) { AspectjCompile compile ->
         compile.aspectjClasspath = project.configurations.getByName(ASPECTJTOOLS_CONFIGURATION_NAME)
      }
   }
   
   protected void configureSourceSetDefaults(Project project, JavaBasePlugin javaPlugin) {
      project.convention.getPlugin(JavaPluginConvention).sourceSets.all(new Action() {
         public void execute(sourceSet) {
            ClassGenerator generator = project.services.get(ClassGenerator)
            AspectjSourceSet ass = generator.newInstance(DefaultAspectjSourceSet, sourceSet.displayName, project.fileResolver)
            ass.conventionMapping.notWeavedClassesDir = { new File(sourceSet.classesDir.parent, "${sourceSet.name}-not-weaved") }
            sourceSet.convention.plugins.aspectj = ass
            sourceSet.aspectj.srcDir { project.file("src/${sourceSet.name}/aspectj") }
            sourceSet.allJava.add(sourceSet.aspectj.matching(sourceSet.java.filter))
            sourceSet.allSource.add(sourceSet.aspectj)
            sourceSet.resources.filter.exclude { FileTreeElement element -> sourceSet.aspectj.contains(element.file) }

            project.tasks.getByName(sourceSet.compileJavaTaskName).conventionMapping.destinationDir = { sourceSet.notWeavedClassesDir }
            
            String taskName = sourceSet.getCompileTaskName("aspectj")
            AspectjCompile aspectjCompile = project.tasks.add(taskName, AspectjCompile)
            aspectjCompile.dependsOn sourceSet.compileJavaTaskName
            javaPlugin.configureForSourceSet(sourceSet, aspectjCompile)
            aspectjCompile.description = "Compiles the ${sourceSet.aspectj}."
            aspectjCompile.conventionMapping.defaultSource = { sourceSet.aspectj + sourceSet.java }
            aspectjCompile.conventionMapping.aspectjSource = { sourceSet.aspectj }
            aspectjCompile.conventionMapping.javaClasses = { sourceSet.notWeavedClassesDir }
            
            project.tasks[sourceSet.classesTaskName].dependsOn(taskName)
         }
      })
   }

}
package cz.jiradesign.gradle.api.plugins.aspectj

import org.gradle.api.tasks.JavaExec
import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.FileCollection
import org.gradle.api.logging.LogLevel
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.TaskAction

import org.gradle.plugins.ide.eclipse.EclipsePlugin
/**
 * @author Jiri Mares (jiramares@gmail.com)
 */
class AspectJPlugin implements Plugin<Project> {

    void apply(Project project) {
        project.plugins.apply(JavaPlugin)
        /*project.plugins.whenPluginAdded { plugin ->
           if (plugin.class == EclipsePlugin) {
               project.plugins.apply(AspectjEclipsePlugin)
           }
        }*/

        def aspectj = project.extensions.create('aspectj', AspectJExtension, project)

        if (project.configurations.findByName('ajtools') == null) {
            project.configurations.create('ajtools')
            project.afterEvaluate { p ->
                if (aspectj.version == null) {
                    throw new GradleException("No aspectj version supplied")
                }

                p.dependencies {
                    ajtools "org.aspectj:aspectjtools:${aspectj.version}"
                    compile "org.aspectj:aspectjrt:${aspectj.version}"
                }
            }
        }

        for (projectSourceSet in project.sourceSets) {
            def namingConventions = projectSourceSet.name.equals('main') ? new MainNamingConventions() : new DefaultNamingConventions();
            for (configuration in [namingConventions.getAspectPathConfigurationName(projectSourceSet), namingConventions.getAspectInpathConfigurationName(projectSourceSet)]) {
                if (project.configurations.findByName(configuration) == null) {
                    project.configurations.create(configuration)
                }
            }

            if (!projectSourceSet.allJava.isEmpty()) {
                def aspectTaskName = namingConventions.getAspectCompileTaskName(projectSourceSet)
                def javaTaskName = namingConventions.getJavaCompileTaskName(projectSourceSet)
                def resourceTaskName =namingConventions. getResourceProcessTaskName(projectSourceSet)

                def aspectjDir = project.file("src/${projectSourceSet.name}/aspectj")
                def aspectjSourceRoots = aspectjDir.exists() ? project.files(aspectjDir) : project.files()
                
                project.tasks.create(name: aspectTaskName, overwrite: true, description: "Compiles AspectJ Source for ${projectSourceSet.name} source set", type: Ajc) {
                    sourceSet = projectSourceSet
                    destinationDir = projectSourceSet.java.outputDir
                    aspectpath = project.configurations.findByName(namingConventions.getAspectPathConfigurationName(projectSourceSet))
                    inpath = project.files(projectSourceSet.java.outputDir)
                    sourceRoots = aspectjSourceRoots 
                }

                project.tasks[aspectTaskName].dependsOn(project.tasks[javaTaskName])
                project.tasks[resourceTaskName].dependsOn(project.tasks[aspectTaskName])
                project.tasks[aspectTaskName].dependsOn(project.tasks[aspectTaskName].aspectpath)
            }
        }
    }

    private static class MainNamingConventions implements NamingConventions {

        @Override
        String getJavaCompileTaskName(final SourceSet sourceSet) {
            return "compileJava"
        }

        @Override
        String getResourceProcessTaskName(final SourceSet sourceSet) {
            return "processResources"
        }

        @Override
        String getAspectCompileTaskName(final SourceSet sourceSet) {
            return "compileAspectj"
        }

        @Override
        String getAspectPathConfigurationName(final SourceSet sourceSet) {
            return "aspectpath"
        }

        @Override
        String getAspectInpathConfigurationName(final SourceSet sourceSet) {
            return "ajInpath"
        }
    }

    private static class DefaultNamingConventions implements NamingConventions {

        @Override
        String getJavaCompileTaskName(final SourceSet sourceSet) {
            return "compile${sourceSet.name.capitalize()}Java"
        }

        @Override
        String getResourceProcessTaskName(final SourceSet sourceSet) {
            return "process${sourceSet.name.capitalize()}Resources"
        }

        @Override
        String getAspectCompileTaskName(final SourceSet sourceSet) {
            return "compile${sourceSet.name.capitalize()}Aspectj"
        }

        @Override
        String getAspectPathConfigurationName(final SourceSet sourceSet) {
            return "${sourceSet.name}Aspectpath"
        }

        @Override
        String getAspectInpathConfigurationName(final SourceSet sourceSet) {
            return "${sourceSet.name}AjInpath"
        }
    }
}

class Ajc extends JavaExec {

    SourceSet sourceSet

    File destinationDir

    FileCollection aspectpath
    FileCollection inpath
    FileCollection sourceRoots

    // ignore or warning
    String xlint = 'ignore'

    Ajc() {
        logging.captureStandardOutput(LogLevel.INFO)       
    }

    public void exec() {
        logger.info("Running ajc ...")

        main = "org.aspectj.tools.ajc.Main"
        classpath = project.configurations.ajtools
        args = [ "-inpath", inpath.asPath,
                 "-sourceroots", sourceRoots.asPath, 
                 "-classpath", sourceSet.compileClasspath.asPath,
                 "-d", destinationDir,
                 "-target", project.convention.plugins.java.targetCompatibility,
                 "-source", project.convention.plugins.java.targetCompatibility,
                 "-showWeaveInfo",
                 "-Xlint:" + xlint ]

        super.exec()
    }
}

class AspectJExtension {

    String version

    AspectJExtension(Project project) {
        this.version = project.findProperty('aspectjVersion') ?: '1.8.12'
    }
}

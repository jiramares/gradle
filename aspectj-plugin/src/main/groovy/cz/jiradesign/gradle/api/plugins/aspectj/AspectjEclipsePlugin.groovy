package cz.jiradesign.gradle.api.plugins.aspectj

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.plugins.ide.eclipse.GenerateEclipseClasspath
import org.gradle.plugins.ide.eclipse.GenerateEclipseProject

/**
 *
 * @author Jiri Mares (jiramares@gmail.com) 
 */ 
class AspectjEclipsePlugin implements Plugin<Project> {
    
    public void apply(Project project) {
       configureGenerateEclipseProject(project)
       configureGenerateEclipseClasspath(project)
    }
    
    protected void configureGenerateEclipseProject(Project project) {
       project.tasks.withType(GenerateEclipseProject) {
          withXml() { xmlProvider->
             def projectDescription = xmlProvider.asNode()
             def xmlparser = new XmlParser()

             def builders = projectDescription.buildSpec[0]
             def ajbuilder = xmlparser.createNode(builders, 'buildCommand', [:])
             xmlparser.createNode(ajbuilder, 'name', [:]).setValue('org.eclipse.ajdt.core.ajbuilder')
             xmlparser.createNode(ajbuilder, 'arguments', [:]);

             def natures = projectDescription.natures[0]
             def ajnature = xmlparser.createNode(null, 'nature', [:])
             ajnature.setValue('org.eclipse.ajdt.ui.ajnature');
             natures.children().add(0, ajnature)
          }
       }
    }
    
    protected void configureGenerateEclipseClasspath(Project project) {
       project.tasks.withType(GenerateEclipseClasspath) {
          withXml { xmlProvider ->
             def classpath = xmlProvider.asNode()
             def xmlparser = new XmlParser()

             project.configurations[AspectjBasePlugin.ASPECTJASPECTS_CONFIGURATION_NAME].files.each { lib ->
                classpath.children().findAll { it['@path'] == lib.absolutePath }.each {
                   def attrs = xmlparser.createNode(it, 'attributes', [:])
                   xmlparser.createNode(attrs, 'attribute', [name: 'org.eclipse.ajdt.aspectpath', value: 'true']);
                }
             }
          }
       }
    }

}
package cz.jiramares.gradle.api.plugins.aspectj

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin

/**
 * The AspectJ plugin extends the Java plugin to add support for aspectj to gradle build system.
 *
 * @author Jiri Mares (jiramares@gmail.com) 
 */ 
class AspectjPlugin implements Plugin<Project> {
    
    public void apply(Project project) {
       project.plugins.apply(JavaPlugin.class)
       project.plugins.apply(AspectjBasePlugin.class)
    }

}
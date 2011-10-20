package cz.jiradesign.gradle.api.tasks

import groovy.lang.Closure;
import org.gradle.api.file.SourceDirectorySet;

/**
 * 
 * @author Jiri Mares (jiramares@gmail.com) 
 */
interface AspectjSourceSet {
   
   SourceDirectorySet getAspectj()
   
   AspectjSourceSet aspectj(Closure configureClosure)
   
   SourceDirectorySet getAllAspectj()
   
   File getNotWeavedClassesDir()
   
   void setNotWeavedClassesDir(File notWeavedClassesDir)
   
}
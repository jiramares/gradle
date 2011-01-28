package cz.jiramares.gradle.api.tasks

import groovy.lang.Closure;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.SourceDirectorySet;

interface AspectjSourceSet {
   
   SourceDirectorySet getAspectj()
   
   AspectjSourceSet aspectj(Closure configureClosure)
   
   FileTree getAllAspectj()
   
   File getNotWeavedClassesDir()
   
   void setNotWeavedClassesDir(File notWeavedClassesDir)
   
}
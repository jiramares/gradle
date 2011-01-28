package cz.jiramares.gradle.api.internal.tasks.aspectj

import org.gradle.api.internal.tasks.compile.Compiler
import cz.jiramares.gradle.api.tasks.aspectj.AspectjCompileOptions
import org.gradle.api.file.FileCollection

/**
 * 
 * @author Jiri Mares (jiramares@gmail.com) 
 */
interface AspectjCompiler extends Compiler {
   
   AspectjCompileOptions getAspectjCompileOptions()
   
   void setAspectjClasspath(Iterable classpath)
   
   void setAspectsClasspath(Iterable classpath)
   
   void setAspectjSource(FileCollection aspectjSource)
   
   void setJavaClasses(File javaClasses)
}
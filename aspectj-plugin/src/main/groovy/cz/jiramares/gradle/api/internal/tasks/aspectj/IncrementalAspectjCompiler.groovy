package cz.jiramares.gradle.api.internal.tasks.aspectj

import org.gradle.api.internal.tasks.compile.IncrementalJavaSourceCompiler
import org.gradle.api.internal.TaskOutputsInternal
import org.gradle.api.internal.tasks.compile.SimpleStaleClassCleaner
import org.gradle.api.internal.tasks.compile.StaleClassCleaner
import cz.jiramares.gradle.api.tasks.aspectj.AspectjCompileOptions
import org.gradle.api.file.FileCollection

/**
 * 
 * @author Jiri Mares (jiramares@gmail.com) 
 */
class IncrementalAspectjCompiler extends IncrementalJavaSourceCompiler implements AspectjJavaJointCompiler {
   protected  TaskOutputsInternal taskOutputs

   public IncrementalAspectjCompiler(AspectjJavaJointCompiler compiler, TaskOutputsInternal taskOutputs) {
      super(compiler)
      this.taskOutputs = taskOutputs
   }

   public AspectjCompileOptions getAspectjCompileOptions() {
      return compiler.aspectjCompileOptions
   }

   public void setAspectjClasspath(Iterable<File> classpath) {
      compiler.aspectjClasspath = classpath
   }
   
   public void setAspectsClasspath(Iterable<File> classpath) {
      compiler.aspectsClasspath = classpath
   }
   
   public void setAspectjSource(FileCollection aspectjSource) {
      compiler.aspectjSource = aspectjSource
   }

   public void setJavaClasses(File javaClasses) {
      compiler.javaClasses = javaClasses
   }

   protected StaleClassCleaner createCleaner() {
      return new SimpleStaleClassCleaner(taskOutputs);
   }
}
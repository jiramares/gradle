package cz.jiramares.gradle.api.internal.tasks.aspectj

import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.gradle.api.internal.ClassPathRegistry
import org.gradle.api.tasks.util.PatternSet

import org.gradle.api.tasks.WorkResult
import org.gradle.api.tasks.compile.CompileOptions
import cz.jiramares.gradle.api.tasks.aspectj.AspectjCompileOptions

/**
 * 
 * @author Jiri Mares (jiramares@gmail.com) 
 */
class AntAspectjCompiler implements AspectjJavaJointCompiler {
   public static final Logger logger = LoggerFactory.getLogger(AntAspectjCompiler)

   private final IsolatedAntBuilder ant
   private final ClassPathRegistry classPathRegistry
   FileCollection source
   FileCollection aspectjSource
   File javaClasses
   File destinationDir
   Iterable<File> classpath
   Iterable<File> aspectsClasspath
   Iterable<File> aspectjClasspath
   String sourceCompatibility
   String targetCompatibility
   AspectjCompileOptions aspectjCompileOptions = new AspectjCompileOptions()
   CompileOptions compileOptions = new CompileOptions()

   def AntAspectjCompiler(IsolatedAntBuilder ant, ClassPathRegistry classPathRegistry) {
       this.ant = ant;
       this.classPathRegistry = classPathRegistry;
   }

   public WorkResult execute() {
      Map otherArgs = [
            inpath: javaClasses,
            destdir: destinationDir,
            target: targetCompatibility,
            source: sourceCompatibility
      ]
      Map options = otherArgs + aspectjCompileOptions.optionMap()
      logger.debug("Running ant iajc with the following options {}", options)
      
      ant.withClasspath(aspectjClasspath).execute { ant ->
           taskdef(resource: 'org/aspectj/tools/ant/taskdefs/aspectjTaskdefs.properties')
           iajc(options) {
              aspectjSource.addToAntBuilder(ant, 'sourceroots', FileCollection.AntType.MatchingTask)
              classpath.each { file -> classpath(location: file) }
              aspectsClasspath.each { file -> aspectpath(location: file) }
           }
       }

       return { true } as WorkResult
   }
   
   
   
}
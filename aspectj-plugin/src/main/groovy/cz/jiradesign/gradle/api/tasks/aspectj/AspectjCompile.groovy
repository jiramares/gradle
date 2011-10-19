package cz.jiradesign.gradle.api.tasks.aspectj

import org.gradle.api.tasks.compile.AbstractCompile
import org.gradle.api.file.FileCollection
import org.gradle.api.internal.project.IsolatedAntBuilder
import org.gradle.api.internal.ClassPathRegistry
import cz.jiradesign.gradle.api.internal.tasks.aspectj.AspectjJavaJointCompiler
import cz.jiradesign.gradle.api.internal.tasks.aspectj.IncrementalAspectjCompiler
import cz.jiradesign.gradle.api.internal.tasks.aspectj.AntAspectjCompiler
import org.gradle.api.tasks.Nested
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.compile.CompileOptions
import org.gradle.api.file.FileTree
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.file.SourceDirectorySet

class AspectjCompile extends AbstractCompile {
   protected AspectjJavaJointCompiler compiler
   @InputFiles FileCollection aspectjClasspath
   @InputFiles @Optional FileCollection aspectsClasspath
   @InputFiles FileCollection aspectjSource
   @InputFiles File javaClasses
   
   public AspectjCompile() {
      IsolatedAntBuilder antBuilder = services.get(IsolatedAntBuilder.class)
      ClassPathRegistry classPathRegistry = services.get(ClassPathRegistry.class)
      compiler = new IncrementalAspectjCompiler(new AntAspectjCompiler(antBuilder, classPathRegistry), getOutputs())
   }
   
   protected void compile() {
      compiler.source = source
      compiler.destinationDir = destinationDir
      compiler.classpath = classpath
      compiler.sourceCompatibility = sourceCompatibility
      compiler.targetCompatibility = targetCompatibility
      compiler.aspectjClasspath = aspectjClasspath
      compiler.aspectsClasspath = aspectsClasspath
      compiler.aspectjSource = getAspectjSource()
      compiler.javaClasses = getJavaClasses()
      compiler.execute();
   }

   @Nested
   public AspectjCompileOptions getAspectjOptions() {
      return compiler.aspectjCompileOptions
   }

   @Nested
   public CompileOptions getOptions() {
      return compiler.compileOptions
   }

}
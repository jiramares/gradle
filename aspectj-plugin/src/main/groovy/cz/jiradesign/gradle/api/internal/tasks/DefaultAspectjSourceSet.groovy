package cz.jiradesign.gradle.api.internal.tasks

import cz.jiradesign.gradle.api.tasks.AspectjSourceSet
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.util.ConfigureUtil;
import groovy.lang.Closure;

/**
 * 
 * @author Jiri Mares (jiramares@gmail.com) 
 */
class DefaultAspectjSourceSet implements AspectjSourceSet {
   protected SourceDirectorySet aspectj
   protected File notWeavedClassesDir
   private final SourceDirectorySet allAspectj;

   public DefaultAspectjSourceSet(String displayName, FileResolver fileResolver) {
       aspectj = new DefaultSourceDirectorySet("${displayName} Aspectj source", fileResolver)
       aspectj.getFilter().include("**/*.java", "**/*.aj")
       allAspectj = new DefaultSourceDirectorySet(String.format("%s Aspectj source", displayName), fileResolver);
       allAspectj.getFilter().include("**/*.aj");
       allAspectj.source(aspectj);
   }

   public SourceDirectorySet getAspectj() {
       return aspectj
   }

   public AspectjSourceSet aspectj(Closure configureClosure) {
       ConfigureUtil.configure(configureClosure, getAspectj())
       return this
   }

   public SourceDirectorySet getAllAspectj() {
       return allAspectj
   }
   
   File getNotWeavedClassesDir() {
      return notWeavedClassesDir
   }
   
   void setNotWeavedClassesDir(File notWeavedClassesDir) {
      this.notWeavedClassesDir = fileResolver.resolve(notWeavedClassesDir)
   }

}
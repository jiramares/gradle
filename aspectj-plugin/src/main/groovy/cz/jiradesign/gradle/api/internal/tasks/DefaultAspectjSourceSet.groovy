package cz.jiradesign.gradle.api.internal.tasks

import cz.jiradesign.gradle.api.tasks.AspectjSourceSet
import org.gradle.api.file.FileTree;
import org.gradle.api.file.SourceDirectorySet;
import org.gradle.api.internal.file.UnionFileTree;
import org.gradle.api.internal.file.DefaultSourceDirectorySet;
import org.gradle.api.internal.file.FileResolver;
import org.gradle.api.tasks.util.PatternFilterable;
import org.gradle.api.tasks.util.PatternSet;
import org.gradle.util.ConfigureUtil;
import groovy.lang.Closure;

/**
 * 
 * @author Jiri Mares (jiramares@gmail.com) 
 */
class DefaultAspectjSourceSet implements AspectjSourceSet {
   protected SourceDirectorySet aspectj
   protected UnionFileTree allAspectj
   protected PatternFilterable aspectjPatterns = new PatternSet()
   protected File notWeavedClassesDir

   public DefaultAspectjSourceSet(String displayName, FileResolver fileResolver) {
       aspectj = new DefaultSourceDirectorySet("${displayName} Aspectj source", fileResolver)
       aspectj.getFilter().include("**/*.java", "**/*.aj")
       aspectjPatterns.include("**/*.aj")
       allAspectj = new UnionFileTree("${displayName} Aspectj source", aspectj.matching(aspectjPatterns))
   }

   public SourceDirectorySet getAspectj() {
       return aspectj
   }

   public AspectjSourceSet aspectj(Closure configureClosure) {
       ConfigureUtil.configure(configureClosure, getAspectj())
       return this
   }

   public PatternFilterable getAspectjSourcePatterns() {
       return aspectjPatterns
   }

   public FileTree getAllAspectj() {
       return allAspectj
   }
   
   File getNotWeavedClassesDir() {
      return notWeavedClassesDir
   }
   
   void setNotWeavedClassesDir(File notWeavedClassesDir) {
      this.notWeavedClassesDir = fileResolver.resolve(notWeavedClassesDir)
   }

}
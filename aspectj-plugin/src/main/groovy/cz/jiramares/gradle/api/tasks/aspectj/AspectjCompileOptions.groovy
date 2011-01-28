package cz.jiramares.gradle.api.tasks.aspectj

import org.gradle.api.tasks.compile.AbstractOptions
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional

class AspectjCompileOptions extends AbstractOptions {
   boolean failOnError = true
   boolean showWeaveInfo = true
   @Input
   boolean debug = true
   @Input @Optional
   String encoding = null
   boolean fork = false
   AspectjForkOptions forkOptions = new AspectjForkOptions()
   @Input @Optional
   String bootClasspath = null
   @Input @Optional
   String extensionDirs = null

   AspectjCompileOptions fork(Map forkArgs) {
       fork = true
       forkOptions.define(forkArgs)
       this
   }

   List excludedFieldsFromOptionMap() {
       ['forkOptions']
   }

   Map fieldName2AntMap() {
       [
               failOnError: 'failonerror',
               bootClasspath: 'bootclasspath',
               extensionDirs: 'extDirs',
       ]
   }

   Map optionMap() {
       super.optionMap() + forkOptions.optionMap()
   }

}
package cz.jiradesign.gradle.api.tasks.aspectj

import org.gradle.api.tasks.compile.AbstractOptions

/**
 * 
 * @author Jiri Mares (jiramares@gmail.com) 
 */
class AspectjForkOptions extends AbstractOptions {
    String memoryMaximumSize = null
}
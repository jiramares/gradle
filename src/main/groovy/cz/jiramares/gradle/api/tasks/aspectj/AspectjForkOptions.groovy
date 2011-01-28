package cz.jiramares.gradle.api.tasks.aspectj

import org.gradle.api.tasks.compile.AbstractOptions

class AspectjForkOptions extends AbstractOptions {
    String memoryMaximumSize = null
}
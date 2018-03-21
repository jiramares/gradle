package cz.jiradesign.gradle.api.plugins.aspectj

import org.gradle.api.tasks.SourceSet

public interface NamingConventions {

    String getJavaCompileTaskName(SourceSet sourceSet);

    String getResourceProcessTaskName(SourceSet sourceSet);

    String getAspectCompileTaskName(SourceSet sourceSet);

    String getAspectPathConfigurationName(SourceSet sourceSet);

    String getAspectInpathConfigurationName(SourceSet sourceSet);
}
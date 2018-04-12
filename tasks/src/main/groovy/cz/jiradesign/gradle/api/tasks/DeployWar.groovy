package cz.jiradesign.gradle.api.task

import org.gradle.api.tasks.bundling.War
import org.gradle.api.file.DuplicatesStrategy

public class DeployWar extends War {
    String warName
    String serverName

    public void setWarName(String warName) {
        this.warName = warName
        if (serverName != null && warName != null) setupWarTask()
    }

    public void setServerName(String serverName) {
        this.serverName = serverName
        if (serverName != null && warName != null) setupWarTask()
    }

    protected setupWarTask() {
        setBaseName(warName)
        setVersion(null)
        setDuplicatesStrategy(DuplicatesStrategy.EXCLUDE)
        setDestinationDir(getProject().file('build/libs/' + serverName))
        webInf { from 'src/deploy/' + serverName + "/" + warName + "/WEB-INF" } 
        metaInf { from 'src/deploy/' + serverName + "/" + warName + "/META-INF" }
    }
}
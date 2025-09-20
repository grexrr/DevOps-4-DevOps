package com.devops4devops.console_backend.gateway;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.devops4devops.console_backend.model.Build;

@Component
public class JenkinsGateway {
    /**
     * Create/Update (multibranch/pipeline) job in Jenkins
     * 
     * Call Jenkins REST API
     * POST /job/{jobName}/config.xml
     * Body: jenkinsfileContent
     * @param appId
     * @param Jenkinsfile
     */
    public void createOrUpdateJob(String appId, String Jenkinsfile){}


    /**
     * Trigger Jenkins build
     * @param appId
     * @param params
     */
    public void triggerBuild(String appId, Map<String, String> params) {
    }

    /**
     * Retrieve build history
     * @param buildId
     * @return
     */
    public List<Build> getBuildLog(String buildId) {
        return null;
    }
}

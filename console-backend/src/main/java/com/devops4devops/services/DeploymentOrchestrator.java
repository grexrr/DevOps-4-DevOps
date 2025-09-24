package com.devops4devops.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devops4devops.gateway.JenkinsGateway;

@Service
public class DeploymentOrchestrator {
    @Autowired
    private JenkinsGateway jenkinsGateway;

    @Autowired
    private AppRegistryService appRegistryService;

    @Autowired
    private PipelineTemplateService templateService;


    /**
     * Console Deployment
     * Build frontend → Build backend → Build image → Start/Update Compose
     */
    public void deploySelf(){

        // 1. Build frontend
        jenkinsGateway.triggerBuild("console-frontend", null);

        // 2. Build backend
        jenkinsGateway.triggerBuild("console-backend", null);

        // 3. Build image
        jenkinsGateway.triggerBuild("console-images", null);

        // 4. Start/Update Compose
        jenkinsGateway.triggerBuild("console-deploy", null);
    }

    public void deployApp(String appId, String targetEnv){
        // 1. Trigger Jenkins build
        jenkinsGateway.triggerBuild(appId, Map.of("targetEnv", targetEnv));

        // 3. Wait for build completion
        // 4. Execute deployment script
        // 5. Update deployment status
    }
}

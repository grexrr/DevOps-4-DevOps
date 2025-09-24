package com.devops4devops.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devops4devops.dto.AppRegistrationRequest;
import com.devops4devops.services.AppRegistryService;



@RestController
public class AppController {
    @Autowired
    private AppRegistryService appRegistryService;
    
    @PostMapping("/api/register-apps")
    public void registerApp(@RequestBody AppRegistrationRequest request) {
        // User fills out the form on the webpage:
        // - Application Name: my-spring-app
        // - Git Repository: https://github.com/user/myapp.git
        // - Branch: main
        // - Template: java-maven-template
        appRegistryService.registerApp(
            request.getName(),
            request.getGitRepo(), 
            request.getBranch(),
            request.getTemplateId()
        );
    }

    @GetMapping("/api/apps")
    public List<Map<String, Object>> listApps() {
        return List.of(
            Map.of("id", "app-1", "name", "my-spring-app", "repo", "https://github.com/user/myapp.git"),
            Map.of("id", "app-2", "name", "another-app", "repo", "https://github.com/user/another.git")
        );
    }

    @GetMapping("/api/pipelines")
    public List<Map<String, Object>> listPipelines() {
        return List.of(
            Map.of("id", "pipe-1", "appId", "app-1", "status", "SUCCESS"),
            Map.of("id", "pipe-2", "appId", "app-2", "status", "RUNNING")
        );
    }

    @GetMapping("/api/metrics")
    public Map<String, Object> getMetrics() {
        return Map.of(
            "totalBuilds", 42,
            "successfulBuilds", 38,
            "failedBuilds", 4,
            "averageBuildTime", "5m 23s"
        );
    }

    @GetMapping("/api/envs")
    public List<Map<String, Object>> listEnvironments() {
        return List.of(
            Map.of("id", "dev", "name", "Development", "status", "UP"),
            Map.of("id", "staging", "name", "Staging", "status", "UP"),
            Map.of("id", "prod", "name", "Production", "status", "UP")
        );
    }

    @GetMapping("/version")
    public Map<String, String> getVersion() {
        return Map.of("version", "0.0.1-SNAPSHOT");
    }
}

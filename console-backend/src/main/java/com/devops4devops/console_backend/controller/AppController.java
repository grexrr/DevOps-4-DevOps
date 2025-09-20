package com.devops4devops.console_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.devops4devops.console_backend.dto.AppRegistrationRequest;
import com.devops4devops.console_backend.services.AppRegistryService;

@RestController
public class AppController {
    @Autowired
    private AppRegistryService appRegistryService;
    
    @PostMapping("/api/apps")
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
}

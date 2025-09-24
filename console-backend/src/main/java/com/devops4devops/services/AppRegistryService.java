package com.devops4devops.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devops4devops.gateway.JenkinsGateway;
import com.devops4devops.model.App;

/**
 * Application Registration Service Class
 * 
 * This class is responsible for managing the registration, querying, updating, and deletion of applications.
 * It provides application management functions related to Git repositories and Jenkins pipelines.
 * 
 * @author GRexrr
 * @version 1.0
 * @since 2025-09-20
 */

@Service
public class AppRegistryService {
    @Autowired
    private JenkinsGateway jenkinsGateway;


    /**
     * Register a new application
     * 
     * @param name Application name
     * @param gitRepo Git repository URL
     * @param gitRepoBranch Git branch name
     * @param jenkinsfilePath Jenkinsfile path
     */
    public void registerApp(
        String name, 
        String gitRepo, 
        String gitRepoBranch,
        String Jenkinsfile
        ){  
            return;
        }

    public List<App> listApps(){
        return null; 
    }

    public App getApp(String id){
        return null;
    }

    public boolean deleteApp(String id){
        return true;
    }
}

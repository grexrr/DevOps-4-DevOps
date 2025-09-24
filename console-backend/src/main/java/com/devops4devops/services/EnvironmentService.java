package com.devops4devops.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.devops4devops.model.Environment;

@Service
public class EnvironmentService {

    /**
     * Definition of dev/local/staging/prod (host/cluster, credentials, Compose/K8s manifest)
     */
    public List<Environment> listEnvironments(){
        return null;
    }

    /**
     * Returns deployment driver (DockerComposeDriver / K8sDriver / SSHDriver)
     */
    public Object resolveTarget(String envId){
        return null;
    }


    //============= Getters & Setters =============
    public Environment getEnvironment(String envId) {
        return null;
    }
    
}
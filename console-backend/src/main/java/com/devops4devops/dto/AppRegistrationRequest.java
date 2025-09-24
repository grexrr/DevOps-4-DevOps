package com.devops4devops.dto;

public class AppRegistrationRequest {
    
    private String name;
    private String gitRepo;
    private String branch;
    private String templateId;

    public String getName() {
        return name;
    }
    
    public String getGitRepo() {
        return gitRepo;
    }
    
    public String getBranch() {
        return branch;
    }
    
    public String getTemplateId() {
        return templateId;
    }
}
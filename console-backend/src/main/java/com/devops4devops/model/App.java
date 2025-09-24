package com.devops4devops.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "apps")
public class App {
    @Id
    private String id;
    
    private String version;         

    public String name;

    private String repository;
    
}

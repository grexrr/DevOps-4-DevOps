package com.devops4devops.console_backend.services;

import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    /**
     * Collect events from Jenkins Webhook
     */ 
    public void ingestBuildEvent(String appId, String buildId, String status, long duration) {
        // Process build event
    }

    /**
     * For frontend charts
     */ 
    public Object queryBuildTrends(String appId, String range) {
        // Query build trend data
        return null;
    }
}

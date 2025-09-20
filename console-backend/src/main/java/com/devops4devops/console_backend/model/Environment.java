package com.devops4devops.console_backend.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "environments")
public class Environment {
    
    @Id
    private String id;
    
    private String name;           // "dev", "staging", "prod"
    private String description;    // 环境描述
    private String type;           // "docker-compose", "kubernetes", "ssh"
    
    // Docker Compose 配置
    private String composeFile;    // docker-compose.yml 路径
    private String composeNetwork;  // 网络名称
    
    // Kubernetes 配置
    private String kubeConfig;     // kubeconfig 文件路径
    private String namespace;      // K8s 命名空间
    
    // SSH 配置
    private String host;           // 服务器地址
    private int port;             // SSH 端口
    private String username;       // SSH 用户名
    private String keyPath;        // SSH 私钥路径
    
    // 通用配置
    private String registryUrl;    // 镜像仓库地址
    private Map<String, String> envVars; // 环境变量
    
    // 构造函数、getter、setter...
}

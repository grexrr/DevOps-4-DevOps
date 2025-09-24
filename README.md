# DevOps-4-DevOps

**DevOps-4-DevOps** is a pluggable multi-application DevOps console Dashboard system. MVP aims at building itself from source code through the entire pipeline: build → test → package → deploy ("deploy itself" as MVP). The extension will also integrate/orchestrate the CICD pipeline of other Apps (locally or on cloud). Frontend uses React (with charts), CI/CD uses Jenkins, containerization uses Docker.

### Project Structure (Ongoing)

```
devops4devops/
├── console-frontend/          # React frontend application
├── console-backend/           # Spring Boot backend application
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   └── resources/
│   │   └── test/
│   ├── pom.xml
│   └── mvnw
├── demo-app/                  # Demo application for testing
├── ops/
│   ├── jenkins/               # Jenkins docker-compose and initialization scripts
│   └── compose/               # Local one-click run docker-compose.yml
├── docs/                      # Architecture diagrams, API conventions, Runbook
├── .gitignore
└── README.md
  ```

### Development Phases (Planned) 

#### Phase 0: Preparation

- Installation: Git, Docker Desktop, JDK 17, Node LTS, docker compose, VS Code
- Create new mono-repo: devops4devops/
    - console-frontend/ (React)
    - console-backend/ (Spring Boot)
    - demo-app/ (Node/Express)
    - ops/jenkins/ (Jenkins Docker orchestration, initialization scripts)
    - ops/compose/ (development docker-compose.yml) - docs/ (architecture diagrams, API conventions, getting started documentation)

#### Phase 1: Get Jenkins Running

- Use docker compose to start a local Jenkins (mount volumes to save configuration).
- Create credentials in Jenkins (for GitHub access), install common plugins.
- Create a seed pipeline or directly create "multibranch pipeline", pointing to this repository.

#### Phase 2: MVP Self-Deployment (minimal functionality is fine)

- Backend:
    - Expose REST: /api/apps, /api/pipelines, /api/metrics, /api/envs (returning fake data is fine, just get frontend working first)
    - Dockerfile: mvn test → mvn package → run JAR

- Frontend:
    - Pages: Dashboard (1 line chart + 2 cards), Apps (list + registration form)
    - Call backend fake APIs, get UI running first

- Jenkinsfile (for console itself):
    - stages: Checkout → Backend Unit Test → Frontend Unit Test → Build Images → Compose Up
    - artifacts: devops-console-backend:local, devops-console-frontend:local

- One-click execution: Click "Build" in Jenkins, see console frontend and backend containers start successfully, access UI

#### Phase 3: Integrate "External App (demo-app)"

- demo-app: Simple Node/Express + route /healthz
- Dockerfile + Jenkinsfile (build → test → package image → deploy to local compose network)
- In console frontend: App registration wizard (fill in repo URL, branch, build type, image name, deployment strategy)
- Backend: AppRegistryService.registerApp() → JenkinsGateway.createOrUpdateJob() → frontend triggers "Deploy", see pipeline run successfully in Jenkins, container starts, /healthz passes

#### Phase 4: Make "Metrics & Visualization" Real

- Jenkins Webhook → MetricsService.ingestBuildEvent(...) store to DB (H2/MongoDB both fine)
- Frontend Dashboard pulls real statistics from backend: recent build success rate, average duration, top failure reasons

#### Phase 5: Testing & Coverage (continuous improvement)

- Backend: JUnit + MockMVC, cover 70% initially, target 90% (documentation recommendation).
- Frontend: React Testing Library; unit tests for basic components and API hooks
- Enforce thresholds in Jenkins (mark red if below threshold)

---

### Dataflow (MVP Phase): User Registers a New Application

#### Step 1: User Action (Frontend)
```
The user fills out the form on the webpage:
- Application Name: my-spring-app
- Git Repository: https://github.com/user/myapp.git
- Branch: main
- Template: java-maven-template
```


#### Step 2: Controller Receives Request

```java
@RestController
public class AppController {
    
    @Autowired
    private AppRegistryService appRegistryService;
    
    @PostMapping("/api/apps")
    public void registerApp(@RequestBody AppRegistrationRequest request) {
        appRegistryService.registerApp(
            request.getName(),
            request.getGitRepo(), 
            request.getBranch(),
            request.getTemplateId()
        );
    }
}
```

#### Step 3: AppRegistryService Processes Business Logic

```java
@Service
public class AppRegistryService {
    
    @Autowired
    private JenkinsGateway jenkinsGateway;
    
    @Autowired
    private PipelineTemplateService templateService;
    
    public void registerApp(String name, String gitRepo, String gitRepoBranch, String templateId) {
        
        // 1. Save application information to the database
        App app = new App(name, gitRepo, gitRepoBranch);
        appRepository.save(app);
        
        // 2. Render Jenkinsfile template
        Map<String, String> context = Map.of(
            "appName", name,
            "gitRepo", gitRepo,
            "gitBranch", gitRepoBranch
        );
        String jenkinsfileContent = templateService.render(templateId, context);
        
        // 3. Create job in Jenkins
        jenkinsGateway.createOrUpdateJob(app.getId(), jenkinsfileContent);
        
        // 4. Configure Git repository information
        jenkinsGateway.configureGitRepo(app.getId(), gitRepo, gitRepoBranch);
    }
}
```

#### Step 4: PipelineTemplateService Renders Template

```java
@Service
public class PipelineTemplateService {
    
    public String render(String templateId, Map<String, String> context) {
        // Load the template file
        String template = loadTemplate(templateId); // "java-maven-template"
        
        // Replace placeholders
        String jenkinsfile = template
            .replace("${appName}", context.get("appName"))
            .replace("${gitRepo}", context.get("gitRepo"))
            .replace("${gitBranch}", context.get("gitBranch"));
            
        return jenkinsfile;
    }
}
```

**Example of Template Content:**
```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git url: '${gitRepo}', branch: '${gitBranch}'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t ${appName}:latest .'
            }
        }
    }
}
```

**Rendered Result:**
```groovy
pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/user/myapp.git', branch: 'main'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t my-spring-app:latest .'
            }
        }
    }
}
```

#### Step 5: JenkinsGateway Communicates with Jenkins

```java
@Component
public class JenkinsGateway {
    
    public void createOrUpdateJob(String appId, String jenkinsfileContent) {
        // Call Jenkins REST API
        // POST /createItem?name=my-spring-app
        // Body: jenkinsfileContent
        
        String jenkinsUrl = "http://jenkins:8080";
        String jobName = "app-" + appId;
        
        // Send HTTP request to Jenkins
        restTemplate.postForObject(
            jenkinsUrl + "/createItem?name=" + jobName,
            jenkinsfileContent,
            String.class
        );
    }
}
```

#### Step 6: User Triggers Deployment

```
The user clicks the "Deploy" button in the console
```

#### Step 7: DeploymentOrchestrator Coordinates Deployment

```java
@Service
public class DeploymentOrchestrator {
    
    @Autowired
    private JenkinsGateway jenkinsGateway;
    
    @Autowired
    private AppRegistryService appRegistryService;
    
    @Autowired
    private EnvironmentService environmentService;
    
    public void deployApp(String appId, String targetEnv) {
        
        // 1. Retrieve application information
        App app = appRegistryService.getApp(appId);
        
        // 2. Get environment configuration
        Environment env = environmentService.getEnvironment(targetEnv);
        
        // 3. Trigger Jenkins build
        jenkinsGateway.triggerBuild(appId, Map.of("targetEnv", targetEnv));
        
        // 4. Wait for build completion
        waitForBuildCompletion(appId);
        
        // 5. Execute deployment script based on environment type
        executeDeploymentScript(appId, env);
        
        // 6. Update deployment status
        updateDeploymentStatus(appId, "DEPLOYED");
    }
}
```

#### Step 8: Jenkins Executes the Pipeline

```
Jenkins receives the build request and starts executing:
1. Checkout code
2. Maven build
3. Run tests
4. Build Docker image
5. Push to image repository
```

#### Step 9: MetricsService Collects Build Events

```java
@Service
public class MetricsService {
    
    public void ingestBuildEvent(String appId, String buildId, String status, long duration) {
        // Store build metrics to database for dashboard visualization
        BuildEvent event = new BuildEvent(appId, buildId, status, duration);
        buildEventRepository.save(event);
    }
}
```

#### Step 10: Deployment Completed

```
The application has been successfully deployed to the target environment,
and the user can see the deployment status in the console dashboard with
real-time metrics and build trends.
```

---

### Dev Log
#### Sept. 19 2025

Outlined the MVP data-flow through structuring the basic components:

- Created the mono-repo basic structure
- Set up the Spring Boot backend project framework
- Configured Maven dependencies: Spring Boot 3.5.6, MongoDB, Security, Web, etc.
- Created the basic directory structure: console-frontend/, console-backend/, demo-app/, ops/


##### Data Flow Diagram

```
User → Controller → AppRegistryService → {
├── PipelineTemplateService (Render Template)
└── JenkinsGateway (Create Jenkins Job)
}
User → Controller → DeploymentOrchestrator → {
├── AppRegistryService (Retrieve App Information)
├── EnvironmentService (Get Environment Config)
└── JenkinsGateway (Trigger Build)
}
Jenkins → Webhook → MetricsService → Database (Build Events)
Frontend → Controller → MetricsService → Dashboard Charts
```

##### Key Components for MVP Implementation

1. **Controller Layer**: Handle HTTP requests and responses
2. **Service Layer**: Business logic orchestration
   - `AppRegistryService`: Application management
   - `PipelineTemplateService`: Template rendering
   - `DeploymentOrchestrator`: Deployment coordination
   - `EnvironmentService`: Environment configuration
   - `MetricsService`: Build metrics collection
3. **Gateway Layer**: External system integration
   - `JenkinsGateway`: Jenkins API communication
4. **Model Layer**: Data entities
   - `App`: Application information
   - `Environment`: Environment configuration
   - `Build`: Build information
   - `PipelineTemplate`: Template definitions
5. **Repository Layer**: Data persistence (to be implemented)


#### Sept. 22 2025

- Successfully built and ran the Jenkins container locally (`devops4devops-jenkins`).
- Configured GitHub credentials, allowing Jenkins to access and scan the repository.
- Created a Multibranch Pipeline pointing to the `DevOps-4-DevOps` repository.
- Added a minimal Jenkinsfile in the repository to verify Jenkins Pipeline recognition.
- Achieved Phase 1 goals: Jenkins is running, can connect to GitHub, and execute the simplest pipeline.

**Current Dockerfile Achievements**
- Based on the `jenkins/jenkins:lts-jdk17` image.
- Installed Docker CLI, Buildx, and Compose plugins within the container (via Docker's official apt source).
- Ensured Jenkins container can run `docker build` / `docker compose` and control the host Docker by mounting the host's `/var/run/docker.sock`.
- Pre-installed common plugins (git, github, workflow-aggregator, blueocean, docker-workflow).

**Current docker-compose.yml Achievements**
- Defined the `jenkins` service, building the image based on the above Dockerfile.
- Exposed ports:
  - 8080 → Jenkins UI
  - 50000 → JNLP agent (potential future use)
- Mounted volumes:
  - `jenkins_home:/var/jenkins_home` → Persist Jenkins configurations, plugins, and job history.
  - `/var/run/docker.sock:/var/run/docker.sock` → Allows Jenkins inside the container to call the host Docker.
- Restart policy: `unless-stopped`, ensuring the container restarts automatically if it exits unexpectedly.

**Current Jenkinsfile Achievements**
- Located in the root directory of the repository.
- Minimal pipeline definition with two stages:
  - `Checkout`: Pulls the source code.
  - `Hello`: Prints a verification message to confirm pipeline execution.
- Purpose: Placeholder to verify Jenkins Multibranch Pipeline can find and execute the Jenkinsfile.
- Next step (Phase 2) will expand into a real build and deployment process (testing, image packaging, compose up).

**GitHub Credentials**
- Generated a **GitHub Personal Access Token (classic)** with `repo` scope for Jenkins to access the repository.
- In Jenkins → *Manage Jenkins → Credentials → Global*, created credentials using **“Username with password”** type:
  - **Username**: GitHub account username (`grexrr`)
  - **Password**: The generated GitHub PAT (token)
- Note: *Secret text* type credential did not show up in Multibranch Pipeline’s Git SCM configuration. Using *Username with password* allowed Jenkins to properly recognize and authenticate.
- With this credential selected in Multibranch Pipeline configuration, Jenkins successfully connected to GitHub, scanned the repository, and found the Jenkinsfile.

#### Sept. 24 2025

- Backend Minimum Viable APIs
  - Verified all API endpoints working correctly on port 8081
  - Tested `/actuator/health`, `/version`, `/api/apps`, `/api/pipelines`, `/api/metrics`, `/api/envs`
  - Confirmed Spring Boot Actuator configuration working properly

- **Frontend Initialization** (React + Vite)
  - Created React + Vite project from scratch using `npm create vite@latest`
  - Installed required dependencies: Material-UI, Axios, Recharts
  - Created component files: `Dashboard.jsx`, `Apps.jsx`
  - **Issue Encountered**: App.jsx was still using default Vite template instead of custom components
  - **Resolution**: Updated App.jsx to import and use Dashboard and Apps components
  - Configured Vite proxy to connect frontend (port 5173) to backend (port 8081)
  - **Current Status**: Frontend running but showing default Vite interface instead of custom DevOps console
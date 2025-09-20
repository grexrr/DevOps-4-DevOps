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
### Dev Log
#### Sept. 19 2025
- ✅ **项目初始化完成**
  - 创建了mono-repo基础结构
  - 搭建了Spring Boot后端项目框架
  - 配置了Maven依赖：Spring Boot 3.5.6, MongoDB, Security, Web等
  - 创建了基础目录结构：console-frontend/, console-backend/, demo-app/, ops/

- 🔄 **当前状态**
  - 后端：仅有基础启动类，缺少业务逻辑层
  - 前端：目录为空，待创建React项目
  - Jenkins：配置目录为空，待搭建
  - Demo应用：目录为空，待创建

- 📋 **下一步计划**
  - Phase 1: 完善后端基础架构（Controller/Service/Repository层）
  - Phase 2: 实现MVP REST API（/api/apps, /api/pipelines, /api/metrics, /api/envs）
  - Phase 3: 搭建React前端基础页面
  - Phase 4: Jenkins Docker编排配置
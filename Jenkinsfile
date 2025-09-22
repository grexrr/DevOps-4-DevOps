pipeline {
  agent any
  stages {
    stage('Checkout') { steps { checkout scm } }
    stage('Hello')   { steps { sh 'echo "DevOps4DevOps Phase 1 OK: $(date)"' } }
  }
}

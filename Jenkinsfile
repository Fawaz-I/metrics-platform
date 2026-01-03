pipeline {
  agent {
    kubernetes {
      yaml '''
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: jenkins-agent
spec:
  containers:
  - name: maven
    image: maven:3.9.9-eclipse-temurin-21
    command: ["cat"]
    tty: true
    volumeMounts:
    - mountPath: /root/.m2
      name: m2-repo
  - name: docker
    image: docker:26.1-dind
    securityContext:
      privileged: true
    env:
    - name: DOCKER_TLS_CERTDIR
      value: ""
    tty: true
  - name: kubectl
    image: bitnami/kubectl:1.30
    command: ["cat"]
    tty: true
  volumes:
  - name: m2-repo
    emptyDir: {}
'''
    }
  }
  
  environment {
    DOCKERHUB_USERNAME = 'fohwaz'
    DOCKERHUB_PASSWORD = credentials('dockerhub-password')
    // Default to '1' if BUILD_NUMBER is not set (local testing)
    BUILD_NUMBER = "${env.BUILD_NUMBER ?: '1'}"
  }
  
  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }
    
    stage('Build & Test') {
      steps {
        container('maven') {
          sh 'chmod +x mvnw'
          sh './mvnw clean verify -DskipITs'
        }
      }
    }
    
    stage('Build API Container') {
      steps {
        container('docker') {
          // Retry logic in case dind takes a moment to start
          timeout(time: 2, unit: 'MINUTES') {
            waitUntil {
              script {
                try {
                  sh 'docker info'
                  return true
                } catch (Exception e) {
                  return false
                }
              }
            }
          }
          sh 'docker build -t fohwaz/metrics-platform-api:${BUILD_NUMBER} -f metrics-platform-api/Dockerfile .'
        }
      }
    }
    
    stage('Build Worker Container') {
      steps {
        container('docker') {
          sh 'docker build -t fohwaz/metrics-platform-alerts-worker:${BUILD_NUMBER} -f metrics-platform-alerts-worker/Dockerfile .'
        }
      }
    }
    
    stage('Push Images') {
      steps {
        container('docker') {
          sh '''
            echo $DOCKERHUB_PASSWORD | docker login -u $DOCKERHUB_USERNAME --password-stdin
            docker push fohwaz/metrics-platform-api:${BUILD_NUMBER}
            docker push fohwaz/metrics-platform-alerts-worker:${BUILD_NUMBER}
          '''
        }
      }
    }
    
    stage('Deploy to Staging') {
      steps {
        container('kubectl') {
          sh '''
            # Update image tags in deployment manifests
            sed -i "s|image: .*metrics-alerts-worker.*|image: fohwaz/metrics-platform-alerts-worker:${BUILD_NUMBER}|g" infra/k8s/alerts-worker-deployment.yaml
            
            # The API deployment might assume a local image name in dev, we overwrite it fully
            # Targeted replacement to avoid replacing sidecars if any
            sed -i "s|image: .*metrics-platform-api.*|image: fohwaz/metrics-platform-api:${BUILD_NUMBER}|g" infra/k8s/metrics-api-deployment.yaml
            
            # Apply to Kubernetes
            kubectl apply -f infra/k8s/ -n metrics-platform
            
            # Verify rollout
            kubectl rollout status deployment/metrics-api -n metrics-platform
            kubectl rollout status deployment/alerts-worker -n metrics-platform
          '''
        }
      }
    }
  }
  
  post {
    always {
      cleanWs()
    }
  }
}

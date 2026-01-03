# Jenkinsfile Local Testing Guide

This guide describes three methods to test the `Jenkinsfile` pipeline locally before committing changes.

## Method 1: Manual Verification (Recommended for Quick Checks)
Run the pipeline steps manually to catch syntax or configuration errors quickly.

```bash
# 1. Build & Test
./mvnw clean verify -DskipITs

# 2. Build API Container
docker build -t fohwaz/metrics-platform-api:test -f metrics-platform-api/Dockerfile .

# 3. Build Worker Container
docker build -t fohwaz/metrics-platform-alerts-worker:test -f metrics-platform-alerts-worker/Dockerfile .

# 4. Verify Manifest Updates (Dry Run)
# Creates temp file to verify sed replacement
cp infra/k8s/alerts-worker-deployment.yaml temp.yaml
sed -i "s|image: .*|image: fohwaz/metrics-platform-alerts-worker:test|g" temp.yaml
grep "image:" temp.yaml
rm temp.yaml
```

## Method 2: Jenkinsfile Runner (Automated)
Use the official Jenkinsfile Runner docker image to execute the pipeline in a container.

```bash
docker run --rm \
  -v $(pwd):/workspace \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -e CASC_JENKINS_CONFIG=/workspace/jenkins.yaml \
  jenkinsci/jenkinsfile-runner
```
*Note: Requires configuring a `jenkins.yaml` for plugins and agents.*

## Method 3: Local Jenkins Docker (Full Environment)
Run a full Jenkins instance locally to test the Kubernetes agent configuration.

```bash
# 1. Start Jenkins
docker run -it --rm \
  -v $(pwd):/workspace \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -p 8080:8080 \
  jenkins/jenkins:lts-jdk21

# 2. Configure Cloud
# Go to http://localhost:8080 -> Manage Jenkins -> Clouds -> New Cloud -> Kubernetes
# Set Docker URL: unix:///var/run/docker.sock
```

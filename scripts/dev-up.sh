#!/bin/bash
set -e

# Get the project root directory
PROJECT_ROOT="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"

echo "Starting Minikube..."
minikube start --driver=docker

echo "Setting up Docker environment..."
eval $(minikube docker-env)

echo "Building metrics-platform-api image..."
docker build -t metrics-platform-api:latest "${PROJECT_ROOT}/metrics-platform-api"

echo "Building metrics-alerts-worker image..."
docker build -t metrics-alerts-worker:latest -f "${PROJECT_ROOT}/metrics-platform-alerts-worker/Dockerfile" "${PROJECT_ROOT}"

echo "Applying Kubernetes manifests..."
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/namespace.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/app-config.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/postgres-pvc.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/postgres-deployment.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/postgres-service.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/metrics-api-deployment.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/metrics-api-service.yaml"

echo "Deploying alerts-worker..."
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/alerts-worker-deployment.yaml" -n metrics-platform

echo "Waiting for deployments..."
kubectl rollout status deployment/postgres -n metrics-platform
kubectl rollout status deployment/metrics-api -n metrics-platform
kubectl rollout status deployment/alerts-worker -n metrics-platform

echo "Development environment is up!"
echo "Service URL (API):"
minikube service metrics-api -n metrics-platform --url

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

echo "Applying Kubernetes manifests..."
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/namespace.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/postgres-pvc.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/postgres-deployment.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/postgres-service.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/metrics-api-deployment.yaml"
kubectl apply -f "${PROJECT_ROOT}/infra/k8s/metrics-api-service.yaml"

echo "Waiting for deployments..."
kubectl rollout status deployment/postgres -n metrics-platform
kubectl rollout status deployment/metrics-api -n metrics-platform

echo "Development environment is up!"
echo "Service URL:"
minikube service metrics-api -n metrics-platform --url

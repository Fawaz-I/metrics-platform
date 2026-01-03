#!/bin/bash
set -e

echo "Deleting metrics-platform namespace..."
kubectl delete namespace metrics-platform

echo "Development environment torn down."

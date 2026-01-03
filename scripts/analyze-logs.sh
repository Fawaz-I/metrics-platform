#!/bin/bash
set -e

NAMESPACE="metrics-platform"
APP_LABEL="app=metrics-api"

echo "Fetching logs for ${APP_LABEL} in namespace ${NAMESPACE}..."

# Get the pod name
POD_NAME=$(kubectl get pods -n ${NAMESPACE} -l ${APP_LABEL} -o jsonpath="{.items[0].metadata.name}")

if [ -z "$POD_NAME" ]; then
    echo "No pod found for label ${APP_LABEL}"
    exit 1
fi

echo "Analyzing logs from pod: ${POD_NAME}"

# Fetch logs and analyze
# Counting occurrences of ERROR (often associated with 500s) and specific 5xx patterns if visible
kubectl logs -n ${NAMESPACE} "$POD_NAME" | awk '
    BEGIN { error_count=0; exception_count=0 }
    /ERROR/ { error_count++ }
    /Exception/ { exception_count++ }
    END {
        print "--- Log Analysis Report ---"
        print "Total ERROR logs: " error_count
        print "Total Exception logs: " exception_count
    }
'

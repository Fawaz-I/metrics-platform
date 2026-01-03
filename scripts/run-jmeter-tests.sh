#!/bin/bash

if ! command -v jmeter &> /dev/null; then
    echo "Error: jmeter is not installed or not in PATH."
    echo "Please install Apache JMeter to run these tests."
    echo "Download: https://jmeter.apache.org/download_jmeter.cgi"
    exit 1
fi

if [[ "$CI" != "true" ]]; then
    echo "Port-forwarding metrics API to localhost:8080..."
    kubectl port-forward svc/metrics-api 8080:8080 -n metrics-platform > /dev/null 2>&1 &
    PF_PID=$!
    sleep 5
else
    echo "Running in CI mode - assuming localhost:8080 is available..."
fi

echo "Starting JMeter tests..."
echo "Timestamp: $(date +%Y%m%d-%H%M%S)"

mkdir -p test-reports

TIMESTAMP=$(date +%Y%m%d-%H%M%S)

jmeter -n \
    -t infra/jmeter/metrics-ingest-test.jmx \
    -l test-reports/metrics-ingest-${TIMESTAMP}.jtl \
    -e -o test-reports/metrics-ingest-report-${TIMESTAMP}

EXIT_CODE=$?

if [[ "$CI" != "true" ]]; then
    echo "Cleaning up..."
    kill $PF_PID
fi

if [ $EXIT_CODE -eq 0 ]; then
    echo "Tests completed successfully."
    echo "Report generated at: test-reports/metrics-ingest-report-${TIMESTAMP}/index.html"
else
    echo "Tests failed with exit code $EXIT_CODE"
fi

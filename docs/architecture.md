# Metrics & Alerts Platform - Architecture

## Overview

The Metrics & Alerts Platform is a cloud-native microservices solution for ingesting application performance metrics via REST APIs, computing aggregated statistics, and generating threshold-based alerts. Built with Java 21 and Spring Boot 3.5, the platform demonstrates production-grade patterns aligned with Red Hat best practices and is OpenShift-ready for enterprise Kubernetes deployments.

## System Architecture

```
                                    +-----------------------+
                                    |       JMeter          |
                                    |   (Load Testing)      |
                                    +-----------+-----------+
                                                |
                                                v
+------------------+              +-------------+-------------+
|    Jenkins       |              |     Kubernetes Ingress    |
|  (CI/CD Pipeline)|              |     (OpenShift Route)     |
+--------+---------+              +-------------+-------------+
         |                                      |
         | build/test/push                      v
         |                        +-------------+-------------+
         +----------------------->|    metrics-api Pod        |
                                  |    (Spring Boot 3.5)      |
                                  |  POST /metrics            |
                                  |  GET  /stats              |
                                  |  GET  /alerts             |
                                  +-------------+-------------+
                                                |
                         +----------------------+----------------------+
                         |                                             |
                         v                                             v
           +-------------+-------------+             +-----------------+-----------------+
           |   PostgreSQL StatefulSet  |<----------->|    alerts-worker Pod              |
           |   (metrics, alerts tables)|             |    (Scheduled Job Processing)     |
           +---------------------------+             +-----------------------------------+
```

## Planned Components

- **API Service (Spring Boot 3.5)**: REST endpoints for metric ingestion (`POST /metrics`), statistics queries (`GET /stats`), and alert retrieval (`GET /alerts`). Uses Spring Data JPA with Flyway migrations.

- **Alerts Worker**: Scheduled background service computing alert conditions based on configurable thresholds (latency, error rates). Writes alerts to shared PostgreSQL instance.

- **PostgreSQL Database**: Persistent storage with indexed tables for `metric_records` and `alerts`. Deployed as Kubernetes StatefulSet with PersistentVolumeClaims.

- **Kubernetes Manifests**: Deployments, Services, ConfigMaps, Secrets, liveness/readiness probes, and resource limits. OpenShift-compatible with Route definitions for external access.

- **Jenkins Pipeline**: Multi-stage CI/CD with build, unit test, Docker image creation, container registry push, and Kubernetes deployment stages.

- **JMeter Test Suite**: Performance tests measuring API latency (p50/p95/p99), throughput (requests/sec), and error rates under load.

- **Bash Utilities**: Log analysis scripts using grep, awk, and sed for production troubleshooting and metrics extraction.

## Technology Stack

| Layer          | Technology                              |
|----------------|-----------------------------------------|
| Runtime        | Java 21 (LTS)                           |
| Framework      | Spring Boot 3.5, Spring Data JPA        |
| Database       | PostgreSQL 15+                          |
| Migrations     | Flyway                                  |
| Container      | Docker, OpenShift-compatible images     |
| Orchestration  | Kubernetes (minikube, OpenShift-ready)  |
| CI/CD          | Jenkins, Maven                          |
| IaC            | Terraform                               |
| Testing        | JUnit 5, JMeter                         |

## Future Enhancements

- **Observability**: Prometheus metrics export, Grafana dashboards, and distributed tracing with OpenTelemetry
- **Multi-Region**: Active-passive database replication and geo-routing
- **Event Streaming**: Kafka integration for real-time metric ingestion at scale
- **Authentication**: OAuth2/OIDC integration with Red Hat SSO (Keycloak)
- **API Gateway**: Rate limiting, request validation, and API versioning

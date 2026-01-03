FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY . .

WORKDIR /build/metrics-platform-alerts-worker
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=builder --chown=appuser:appgroup /build/metrics-platform-alerts-worker/target/quarkus-app/lib/ /app/lib/
COPY --from=builder --chown=appuser:appgroup /build/metrics-platform-alerts-worker/target/quarkus-app/*.jar /app/
COPY --from=builder --chown=appuser:appgroup /build/metrics-platform-alerts-worker/target/quarkus-app/app/ /app/app/
COPY --from=builder --chown=appuser:appgroup /build/metrics-platform-alerts-worker/target/quarkus-app/quarkus/ /app/quarkus/

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost:8080/q/health || exit 1

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]

package com.fawazilupeju.metrics.worker;

import com.fawazilupeju.metrics.worker.domain.Alert;
import com.fawazilupeju.metrics.worker.domain.AlertStatus;
import com.fawazilupeju.metrics.worker.domain.MetricRecord;
import io.quarkus.logging.Log;
import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;

@ApplicationScoped
public class AlertsWorker {

  @Scheduled(every = "30s")
  @Transactional
  void computeAlerts() {
    Log.info("Starting alert computation job...");

    long metricCount = MetricRecord.count();
    Log.infof("Found %d metrics in database", metricCount);

    Alert alert = new Alert();
    alert.setServiceName("auth-service");
    alert.setType("high_latency");
    alert.setThreshold(200.0);
    alert.setCurrentValue(250.5);
    alert.setStatus(AlertStatus.ACTIVE);
    alert.setCreatedAt(Instant.now());

    alert.persist();

    Log.infof("Created new alert for auth-service: %s", alert.getId());
  }
}

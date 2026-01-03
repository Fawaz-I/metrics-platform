package com.fawazilupeju.metrics.worker;

import com.fawazilupeju.metrics.worker.domain.Alert;
import com.fawazilupeju.metrics.worker.domain.AlertStatus;
import com.fawazilupeju.metrics.worker.domain.MetricRecord;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.Instant;
import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class AlertsConsumer {

  @Incoming("metrics-ingest")
  @Transactional
  public void processMetric(MetricRecord metric) {
    Log.infof("Received metric: %s | %d ms", metric.getServiceName(), metric.getLatencyMs());

    if (metric.getLatencyMs() > 200) {
      createAlert(metric);
    }
  }

  private void createAlert(MetricRecord metric) {
    Alert alert = new Alert();
    alert.setServiceName(metric.getServiceName());
    alert.setType("high_latency");
    alert.setThreshold(200.0);
    alert.setCurrentValue(metric.getLatencyMs().doubleValue());
    alert.setStatus(AlertStatus.ACTIVE);
    alert.setCreatedAt(Instant.now());

    alert.persist();
    Log.warnf(
        "ALERT CREATED: Service %s is slow (%d ms)",
        metric.getServiceName(), metric.getLatencyMs());
  }
}

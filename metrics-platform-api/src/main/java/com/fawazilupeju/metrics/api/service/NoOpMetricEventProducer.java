package com.fawazilupeju.metrics.api.service;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "false", matchIfMissing = true)
public class NoOpMetricEventProducer implements MetricEventProducer {

  private static final Logger log = LoggerFactory.getLogger(NoOpMetricEventProducer.class);

  @Override
  public void sendMetricEvent(MetricRecord metric) {
    log.debug("Kafka disabled - skipping event for metric: {}", metric.getId());
  }
}

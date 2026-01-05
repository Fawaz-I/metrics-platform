package com.fawazilupeju.metrics.api.service;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "kafka.enabled", havingValue = "true")
public class KafkaMetricEventProducer implements MetricEventProducer {

  private final KafkaTemplate<String, MetricRecord> kafkaTemplate;

  public KafkaMetricEventProducer(KafkaTemplate<String, MetricRecord> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @Override
  public void sendMetricEvent(MetricRecord metric) {
    kafkaTemplate.send("metrics-ingest", metric.getServiceName(), metric);
  }
}

package com.fawazilupeju.metrics.api.service;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MetricEventProducer {

  private final KafkaTemplate<String, MetricRecord> kafkaTemplate;

  public MetricEventProducer(KafkaTemplate<String, MetricRecord> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  public void sendMetricEvent(MetricRecord metric) {
    kafkaTemplate.send("metrics-ingest", metric.getServiceName(), metric);
  }
}

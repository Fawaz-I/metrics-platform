package com.fawazilupeju.metrics.api.controller;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import com.fawazilupeju.metrics.api.repository.MetricRecordRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {

  private final MetricRecordRepository repository;
  private final com.fawazilupeju.metrics.api.service.MetricEventProducer producer;

  public MetricsController(
      MetricRecordRepository repository,
      com.fawazilupeju.metrics.api.service.MetricEventProducer producer) {
    this.repository = repository;
    this.producer = producer;
  }

  @PostMapping("/metrics")
  public ResponseEntity<MetricRecord> createMetric(@RequestBody MetricRequest request) {
    MetricRecord record =
        new MetricRecord(
            request.serviceName(),
            request.latencyMs(),
            request.statusCode(),
            request.timestamp() != null ? request.timestamp() : Instant.now());
    MetricRecord saved = repository.save(record);

    producer.sendMetricEvent(saved);

    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  @GetMapping("/metrics/status/{statusCode}")
  public List<MetricRecord> getMetricsByStatus(@PathVariable Integer statusCode) {
    return repository.findByStatusCode(statusCode);
  }

  public record MetricRequest(
      String serviceName, Long latencyMs, Integer statusCode, Instant timestamp) {}
}

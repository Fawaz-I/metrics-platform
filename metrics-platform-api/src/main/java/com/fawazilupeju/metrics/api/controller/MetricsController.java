package com.fawazilupeju.metrics.api.controller;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import com.fawazilupeju.metrics.api.repository.MetricRecordRepository;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MetricsController {

  private final MetricRecordRepository repository;

  public MetricsController(MetricRecordRepository repository) {
    this.repository = repository;
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
    return ResponseEntity.status(HttpStatus.CREATED).body(saved);
  }

  public record MetricRequest(
      String serviceName, Long latencyMs, Integer statusCode, Instant timestamp) {}
}

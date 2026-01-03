package com.fawazilupeju.metrics.worker.domain;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "metric_records")
public class MetricRecord extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "service_name", nullable = false)
  private String serviceName;

  @Column(name = "latency_ms", nullable = false)
  private Long latencyMs;

  @Column(name = "status_code", nullable = false)
  private Integer statusCode;

  @Column(nullable = false)
  private Instant timestamp;

  public MetricRecord() {}

  public MetricRecord(String serviceName, Long latencyMs, Integer statusCode, Instant timestamp) {
    this.serviceName = serviceName;
    this.latencyMs = latencyMs;
    this.statusCode = statusCode;
    this.timestamp = timestamp;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getServiceName() {
    return serviceName;
  }

  public void setServiceName(String serviceName) {
    this.serviceName = serviceName;
  }

  public Long getLatencyMs() {
    return latencyMs;
  }

  public void setLatencyMs(Long latencyMs) {
    this.latencyMs = latencyMs;
  }

  public Integer getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(Integer statusCode) {
    this.statusCode = statusCode;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Instant timestamp) {
    this.timestamp = timestamp;
  }
}

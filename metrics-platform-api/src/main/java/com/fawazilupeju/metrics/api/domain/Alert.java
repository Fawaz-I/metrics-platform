package com.fawazilupeju.metrics.api.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "alerts")
public class Alert {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Column(name = "service_name", nullable = false)
  private String serviceName;

  @Column(nullable = false)
  private String type;

  @Column(nullable = false)
  private Double threshold;

  @Column(name = "current_value", nullable = false)
  private Double currentValue;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private AlertStatus status;

  @Column(name = "created_at", nullable = false)
  private Instant createdAt;

  public Alert() {}

  public Alert(
      String serviceName,
      String type,
      Double threshold,
      Double currentValue,
      AlertStatus status,
      Instant createdAt) {
    this.serviceName = serviceName;
    this.type = type;
    this.threshold = threshold;
    this.currentValue = currentValue;
    this.status = status;
    this.createdAt = createdAt;
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

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public Double getThreshold() {
    return threshold;
  }

  public void setThreshold(Double threshold) {
    this.threshold = threshold;
  }

  public Double getCurrentValue() {
    return currentValue;
  }

  public void setCurrentValue(Double currentValue) {
    this.currentValue = currentValue;
  }

  public AlertStatus getStatus() {
    return status;
  }

  public void setStatus(AlertStatus status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }
}

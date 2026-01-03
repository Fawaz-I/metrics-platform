package com.fawazilupeju.metrics.api.controller;

import com.fawazilupeju.metrics.api.domain.Alert;
import com.fawazilupeju.metrics.api.repository.AlertRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class AlertsController {

  private final AlertRepository repository;

  public AlertsController(AlertRepository repository) {
    this.repository = repository;
  }

  @GetMapping
  public ResponseEntity<List<Alert>> getRecentAlerts() {
    List<Alert> alerts = repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    return ResponseEntity.ok(alerts.stream().limit(20).toList());
  }
}

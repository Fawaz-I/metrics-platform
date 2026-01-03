package com.fawazilupeju.metrics.api.controller;

import com.fawazilupeju.metrics.api.repository.MetricRecordRepository;
import com.fawazilupeju.metrics.api.repository.ServiceStats;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatsController {

  private final MetricRecordRepository repository;

  public StatsController(MetricRecordRepository repository) {
    this.repository = repository;
  }

  @GetMapping("/stats")
  public ResponseEntity<List<ServiceStats>> getStats() {
    List<ServiceStats> stats = repository.getServiceStats();
    return ResponseEntity.ok(stats);
  }
}

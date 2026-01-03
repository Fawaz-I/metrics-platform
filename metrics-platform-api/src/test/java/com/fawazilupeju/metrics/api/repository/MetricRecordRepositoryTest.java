package com.fawazilupeju.metrics.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class MetricRecordRepositoryTest {

  @Autowired private MetricRecordRepository repository;

  @Test
  void shouldSaveMetricRecord() {
    MetricRecord metric = new MetricRecord();
    metric.setServiceName("auth-service");
    metric.setLatencyMs(250L);
    metric.setStatusCode(200);
    metric.setTimestamp(Instant.now());

    MetricRecord saved = repository.save(metric);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getServiceName()).isEqualTo("auth-service");
  }

  @Test
  void shouldFindMetricRecordById() {
    MetricRecord metric = new MetricRecord("payment-service", 100L, 201, Instant.now());
    MetricRecord saved = repository.save(metric);

    MetricRecord found = repository.findById(saved.getId()).orElse(null);

    assertThat(found).isNotNull();
    assertThat(found.getServiceName()).isEqualTo("payment-service");
    assertThat(found.getLatencyMs()).isEqualTo(100L);
    assertThat(found.getStatusCode()).isEqualTo(201);
  }
}

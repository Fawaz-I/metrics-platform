package com.fawazilupeju.metrics.api.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.fawazilupeju.metrics.api.domain.Alert;
import com.fawazilupeju.metrics.api.domain.AlertStatus;
import java.time.Instant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class AlertRepositoryTest {

  @Autowired private AlertRepository repository;

  @Test
  void shouldSaveAlert() {
    Alert alert = new Alert();
    alert.setServiceName("auth-service");
    alert.setType("HIGH_LATENCY");
    alert.setThreshold(500.0);
    alert.setCurrentValue(750.0);
    alert.setStatus(AlertStatus.ACTIVE);
    alert.setCreatedAt(Instant.now());

    Alert saved = repository.save(alert);

    assertThat(saved.getId()).isNotNull();
    assertThat(saved.getServiceName()).isEqualTo("auth-service");
    assertThat(saved.getStatus()).isEqualTo(AlertStatus.ACTIVE);
  }

  @Test
  void shouldFindAlertById() {
    Alert alert =
        new Alert(
            "payment-service", "ERROR_RATE", 5.0, 8.5, AlertStatus.ACKNOWLEDGED, Instant.now());
    Alert saved = repository.save(alert);

    Alert found = repository.findById(saved.getId()).orElse(null);

    assertThat(found).isNotNull();
    assertThat(found.getServiceName()).isEqualTo("payment-service");
    assertThat(found.getType()).isEqualTo("ERROR_RATE");
    assertThat(found.getStatus()).isEqualTo(AlertStatus.ACKNOWLEDGED);
  }
}

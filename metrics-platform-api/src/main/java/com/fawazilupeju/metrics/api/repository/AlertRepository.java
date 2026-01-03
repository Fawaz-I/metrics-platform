package com.fawazilupeju.metrics.api.repository;

import com.fawazilupeju.metrics.api.domain.Alert;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertRepository extends JpaRepository<Alert, UUID> {}

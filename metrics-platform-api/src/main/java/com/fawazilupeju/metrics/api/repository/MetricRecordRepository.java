package com.fawazilupeju.metrics.api.repository;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MetricRecordRepository extends JpaRepository<MetricRecord, UUID> {}

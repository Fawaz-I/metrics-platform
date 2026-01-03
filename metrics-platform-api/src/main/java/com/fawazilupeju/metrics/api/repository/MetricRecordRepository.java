package com.fawazilupeju.metrics.api.repository;

import com.fawazilupeju.metrics.api.domain.MetricRecord;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MetricRecordRepository extends JpaRepository<MetricRecord, UUID> {

  @Query(
      "SELECT new com.fawazilupeju.metrics.api.repository.ServiceStats("
          + "m.serviceName, COUNT(m), AVG(m.latencyMs)) "
          + "FROM MetricRecord m GROUP BY m.serviceName")
  List<ServiceStats> getServiceStats();
}

package com.fawazilupeju.metrics.worker;

import com.fawazilupeju.metrics.worker.domain.MetricRecord;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class MetricDeserializer extends ObjectMapperDeserializer<MetricRecord> {
  public MetricDeserializer() {
    super(MetricRecord.class);
  }
}

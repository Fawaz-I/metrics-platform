package com.fawazilupeju.metrics.api.service;

import com.fawazilupeju.metrics.api.domain.MetricRecord;

public interface MetricEventProducer {
  void sendMetricEvent(MetricRecord metric);
}

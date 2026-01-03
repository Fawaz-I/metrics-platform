package com.fawazilupeju.metrics.api;

import com.fawazilupeju.metrics.api.service.MetricEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class MetricsPlatformApiApplicationTests {

  @MockBean private MetricEventProducer metricEventProducer;

  @Test
  void contextLoads() {}
}

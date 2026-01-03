package com.fawazilupeju.metrics.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fawazilupeju.metrics.api.service.MetricEventProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class StatsControllerTest {

  @Autowired private MockMvc mockMvc;
  @MockBean private MetricEventProducer metricEventProducer;

  @Test
  void getStats_returnsAggregatesPerService() throws Exception {
    String json1 =
        """
        {"serviceName": "order-service", "latencyMs": 100, "statusCode": 200}
        """;
    String json2 =
        """
        {"serviceName": "order-service", "latencyMs": 200, "statusCode": 200}
        """;

    mockMvc.perform(post("/metrics").contentType(MediaType.APPLICATION_JSON).content(json1));
    mockMvc.perform(post("/metrics").contentType(MediaType.APPLICATION_JSON).content(json2));

    mockMvc
        .perform(get("/stats"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.serviceName=='order-service')].count").value(2))
        .andExpect(jsonPath("$[?(@.serviceName=='order-service')].avgLatencyMs").value(150.0));
  }
}

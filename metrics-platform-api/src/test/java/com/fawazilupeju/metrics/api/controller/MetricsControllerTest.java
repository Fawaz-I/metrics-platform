package com.fawazilupeju.metrics.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class MetricsControllerTest {

  @Autowired private MockMvc mockMvc;

  @Test
  void postMetric_savesAndReturnsCreated() throws Exception {
    String json =
        """
        {
          "serviceName": "auth-service",
          "latencyMs": 150,
          "statusCode": 200
        }
        """;

    mockMvc
        .perform(post("/metrics").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.serviceName").value("auth-service"))
        .andExpect(jsonPath("$.latencyMs").value(150))
        .andExpect(jsonPath("$.statusCode").value(200))
        .andExpect(jsonPath("$.timestamp").exists());
  }
}

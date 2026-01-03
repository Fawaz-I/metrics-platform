package com.fawazilupeju.metrics.api.repository;

public record ServiceStats(String serviceName, Long count, Double avgLatencyMs) {}

CREATE TABLE metric_records (
    id UUID PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    latency_ms BIGINT NOT NULL,
    status_code INTEGER NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_metric_records_service_name ON metric_records(service_name);
CREATE INDEX idx_metric_records_timestamp ON metric_records(timestamp);

CREATE TABLE alerts (
    id UUID PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    type VARCHAR(100) NOT NULL,
    threshold DOUBLE PRECISION NOT NULL,
    current_value DOUBLE PRECISION NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_alerts_service_name ON alerts(service_name);
CREATE INDEX idx_alerts_status ON alerts(status);
CREATE INDEX idx_alerts_created_at ON alerts(created_at);

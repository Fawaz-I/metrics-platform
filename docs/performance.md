# Performance Optimization Demo

## 1. Baseline / Bottleneck Scenario

**Bottleneck:** Querying `metric_records` by `status_code` without an index.

### Test Setup
- Endpoint: `GET /metrics/status/500` (Querying for non-existent records)
- Database: PostgreSQL (1,000,000 records)
- Load: 50 concurrent users
- Duration: 60 seconds

### Results (Before Optimization)
- **Throughput:** ~18 requests/sec (before crash)
- **Average Latency:** >2000 ms (climbing rapidly)
- **Stability:** **FAILED** (Pod crashed / 100% Error Rate after ~30s)
- **Observation:** Full Table Scan on 1M rows consumed DB CPU/IO, causing thread pool exhaustion and Health Check timeout, leading to K8s restarting the pod.

## 2. Optimization

**Action:** Add index on `status_code`.

```sql
CREATE INDEX idx_metric_records_status_code ON metric_records(status_code);
```

### Results (After Optimization)
- **Throughput:** ~3,172 requests/sec
- **Average Latency:** ~14 ms
- **Stability:** 100% Success
- **Improvement:** >170x throughput increase, massive latency reduction, stable system.

### Conclusion
Adding a simple B-Tree index transformed the query from a linear scan (O(N)) to a logarithmic lookup (O(log N)), preventing resource exhaustion and enabling high-throughput traffic.

package com.exchangerate.currencyexchangeapi;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;
import java.sql.Timestamp;

@Service
public class ApiKeyService {

    private final JdbcTemplate jdbcTemplate;

    public ApiKeyService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public boolean isValidApiKey(String apiKey) {
        String sql = "SELECT COUNT(*) FROM api_key_list WHERE apikey = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, apiKey);
        return count > 0;
    }

    public ApiKeyRecord getApiKeyRecord(String apiKey) {
        String sql = "SELECT max_requests, per_time_unit, request_count, last_request_datetime FROM api_key_list WHERE apikey = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            ApiKeyRecord record = new ApiKeyRecord();
            record.setMaxRequests(rs.getInt("max_requests"));
            record.setPerTimeUnit(TimeUnit.valueOf(rs.getString("per_time_unit")));
            record.setRequestCount(rs.getLong("request_count"));
            record.setLastRequestDatetime(rs.getTimestamp("last_request_datetime"));
            return record;
        }, apiKey);
    }

    public boolean isRateLimitExceeded(String apiKey) {
        ApiKeyRecord apiKeyRecord = getApiKeyRecord(apiKey);
    
        if (apiKeyRecord == null) {
            return false;
        }
    
        int maxRequests = apiKeyRecord.getMaxRequests();
        TimeUnit perTimeUnit = apiKeyRecord.getPerTimeUnit();
        long requestCount = apiKeyRecord.getRequestCount();
        java.sql.Timestamp lastRequestTimestamp = apiKeyRecord.getLastRequestDatetime();
    
        long currentTimeMillis = System.currentTimeMillis();
        long timeDifferenceMillis = currentTimeMillis - lastRequestTimestamp.getTime();
    
        if (timeDifferenceMillis >= perTimeUnit.toMillis(1)) {
            // Reset the request count and update the last request time
            jdbcTemplate.update("UPDATE api_key_list SET request_count = 1, last_request_datetime = ? WHERE apikey = ?", new Timestamp(currentTimeMillis), apiKey);
            return false; // Rate limit not exceeded
        }
    
        if (requestCount >= maxRequests) {
            return true; // Rate limit exceeded
        }
    
        jdbcTemplate.update("UPDATE api_key_list SET request_count = request_count + 1 WHERE apikey = ?", apiKey);
        return false; // Rate limit not exceeded
    }
    
    
}

package com.exchangerate.currencyexchangeapi;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Import for @Transactional

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

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
            // Convert TimeUnit value from string to enum
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

        long currentTime = System.currentTimeMillis();
        long timeDifferenceMillis = currentTime - lastRequestTimestamp.getTime();
        long timeDifferencePerTimeUnit = TimeUnit.MILLISECONDS.convert(timeDifferenceMillis, TimeUnit.MILLISECONDS);

        if (timeDifferencePerTimeUnit > perTimeUnit.toMillis(1)) {
            jdbcTemplate.update("UPDATE api_key_list SET request_count = 1, last_request_datetime = NOW() WHERE apikey = ?", apiKey);
            return false; // Rate limit not exceeded
        } else {
            jdbcTemplate.update("UPDATE api_key_list SET request_count = request_count + 1, last_request_datetime = NOW() WHERE apikey = ?", apiKey);
        }

        if (requestCount >= maxRequests) {
            return true; // Rate limit exceeded
        }

        jdbcTemplate.update("UPDATE api_key_list SET request_count = ?, last_request_datetime = NOW() WHERE apikey = ?", requestCount + 1, apiKey);
        return false;
    }
}

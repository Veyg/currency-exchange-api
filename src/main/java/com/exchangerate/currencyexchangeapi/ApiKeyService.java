package com.exchangerate.currencyexchangeapi;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

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
}

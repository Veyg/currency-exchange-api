package com.exchangerate.currencyexchangeapi;

import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;

public class ApiKeyRecord {
    private int maxRequests;
    private TimeUnit perTimeUnit;
    private long requestCount;
    private Timestamp lastRequestDatetime;

    public int getMaxRequests() {
        return maxRequests;
    }

    public void setMaxRequests(int maxRequests) {
        this.maxRequests = maxRequests;
    }

    public TimeUnit getPerTimeUnit() {
        return perTimeUnit;
    }

    public void setPerTimeUnit(TimeUnit perTimeUnit) {
        this.perTimeUnit = perTimeUnit;
    }

    public long getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(long requestCount) {
        this.requestCount = requestCount;
    }

    public Timestamp getLastRequestDatetime() {
        return lastRequestDatetime;
    }

    public void setLastRequestDatetime(Timestamp lastRequestDatetime) {
        this.lastRequestDatetime = lastRequestDatetime;
    }
}

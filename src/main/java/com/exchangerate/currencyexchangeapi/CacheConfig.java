package com.exchangerate.currencyexchangeapi;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    @Bean
    @Override
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("currencyExchange");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterAccess(1, TimeUnit.HOURS)
                .recordStats();
    }
    
    // The method errorHandler needs to be overridden because we're implementing CachingConfigurer.
    // We're returning null here to keep the default behavior (i.e., not doing any error handling).
    @Override
    public org.springframework.cache.interceptor.CacheErrorHandler errorHandler() {
        return null;
    }
    
    // The method keyGenerator needs to be overridden because we're implementing CachingConfigurer.
    // We're returning null here to keep the default behavior (i.e., not setting a key generator).
    @Override
    public org.springframework.cache.interceptor.KeyGenerator keyGenerator() {
        return null;
    }
    
    // The method cacheResolver needs to be overridden because we're implementing CachingConfigurer.
    // We're returning null here to keep the default behavior (i.e., not setting a cache resolver).
    @Override
    public org.springframework.cache.interceptor.CacheResolver cacheResolver() {
        return null;
    }
}

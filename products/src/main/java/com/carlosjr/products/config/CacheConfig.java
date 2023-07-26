package com.carlosjr.products.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        Caffeine<Object, Object> productCaffeine = Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(20, TimeUnit.SECONDS);
        cacheManager.registerCustomCache("product-cache", productCaffeine.build());

        Caffeine<Object, Object> subProductCaffeine = Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterAccess(60, TimeUnit.SECONDS);
        cacheManager.registerCustomCache("sub-product-cache", subProductCaffeine.build());

        return cacheManager;
    }
}
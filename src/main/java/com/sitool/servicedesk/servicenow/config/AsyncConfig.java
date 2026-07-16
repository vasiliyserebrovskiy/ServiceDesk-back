package com.sitool.servicedesk.servicenow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Enables asynchronous execution and defines a dedicated, bounded thread
 * pool for ServiceNow sync calls, so they don't compete for threads with
 * the rest of the application and don't spawn unbounded threads on a
 * memory-constrained container.
 */
@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "serviceNowTaskExecutor")
    public Executor serviceNowTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(3);
        executor.setQueueCapacity(50);
        executor.setThreadNamePrefix("servicenow-sync-");
        executor.initialize();
        return executor;
    }
}
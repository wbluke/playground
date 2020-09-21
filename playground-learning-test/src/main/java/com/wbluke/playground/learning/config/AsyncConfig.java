package com.wbluke.playground.learning.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@EnableAsync
@Configuration
public class AsyncConfig {

    public static final String LEARNING_DEFAULT_EXECUTOR_NAME = "threadPoolTaskExecutor";
    private static final int POOL_SIZE = 3;

    @Bean(name = LEARNING_DEFAULT_EXECUTOR_NAME)
    public Executor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(POOL_SIZE);
        executor.setMaxPoolSize(POOL_SIZE);
        executor.setThreadNamePrefix("learning-thread-");
        return executor;
    }

}

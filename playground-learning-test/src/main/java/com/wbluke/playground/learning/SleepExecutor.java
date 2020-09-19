package com.wbluke.playground.learning;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class SleepExecutor {

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> sayMessageAfterOneSecond(String message) {
        log.info("start : message = {}", message);

        try {
            Thread.sleep(1000);
            log.info("end : message = {}", message);

            return CompletableFuture.completedFuture("say " + message);
        } catch (Exception e) {
            log.error("sleep throw exception");
            throw new IllegalStateException(e);
        }
    }

}

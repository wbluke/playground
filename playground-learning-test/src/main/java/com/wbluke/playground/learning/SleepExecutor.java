package com.wbluke.playground.learning;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class SleepExecutor {

    @Async("threadPoolTaskExecutor")
    public CompletableFuture<String> sayMessageAfterOneSecond(String message) {
        log.debug("start : message = {}", message);

        try {
            Thread.sleep(1000);
            log.debug("end : message = {}", message);

            return CompletableFuture.completedFuture("say " + message);
        } catch (Exception e) {
            log.error("sleep throw exception");
            throw new IllegalStateException(e);
        }
    }

}

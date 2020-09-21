package com.wbluke.playground.learning;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static com.wbluke.playground.learning.config.AsyncConfig.LEARNING_DEFAULT_EXECUTOR_NAME;

@Slf4j
@Component
public class SleepExecutor {

    @Async(LEARNING_DEFAULT_EXECUTOR_NAME)
    public CompletableFuture<String> sayMessageAfterOneSecond(String message) {
        log.debug("start : message = {}", message);

        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sleep(message));

        log.debug("end : message = {}", message);
        return messageFuture;
    }

    private String sleep(String message) {
        try {
            log.debug("sleep");
            Thread.sleep(1000);

            return "say " + message;
        } catch (Exception e) {
            log.error("sleep throw exception");
            throw new IllegalStateException(e);
        }
    }

}

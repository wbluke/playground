package com.wbluke.playground.learning;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
@RequiredArgsConstructor
@Component
public class SleepExecutor {

    private final Executor threadPoolTaskExecutor;

    public CompletableFuture<String> sayMessageAfterOneSecond(String message) {
        log.debug("start : message = {}", message);

        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sleep(message), threadPoolTaskExecutor);

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

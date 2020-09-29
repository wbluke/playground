package com.wbluke.playground.learning.completablefuture;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
//@SpringBootTest
public class CompletableFutureTest {

    @Autowired
    private Executor threadPoolTaskExecutor;

    @DisplayName("CompletableFuture.supplyAsync()")
    @Test
    void supplyAsync() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture.join();

        /* then */
        log.info("result = {}", result);
        assertThat(result).isEqualTo("say Hello");
    }

    @DisplayName("CompletableFuture.runAsync()")
    @Test
    void runAsync() {
        /* given */
        String message = "Hello";
        CompletableFuture<Void> messageFuture = CompletableFuture.runAsync(() -> sayMessage(message));

        /* when */ /* then */
        messageFuture.join();
    }

    @DisplayName("CompletableFuture.completedFuture()")
    @Test
    void completedFuture() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.completedFuture(message);

        /* when */
        String result = messageFuture.join();

        /* then */
        log.info("result = {}", result);
        assertThat(result).isEqualTo("Hello");
    }

    @DisplayName("CompletableFuture.allOf()")
    @Test
    void allOf() {
        /* given */
        List<String> messages = Arrays.asList("Hello", "Hi", "Bye", "Yes", "No");
        List<CompletableFuture<String>> messageFutures = messages.stream()
                .map(message -> CompletableFuture.supplyAsync(() -> this.sayMessage(message)))
                .collect(Collectors.toList());

        /* when */
        List<String> saidMessages = CompletableFuture.allOf(messageFutures.toArray(new CompletableFuture[0]))
                .thenApply(Void -> messageFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();

        /* then */
        for (String message : saidMessages) {
            log.info("message = {}", message);
        }
    }

    @DisplayName("CompletableFuture.thenApply()")
    @Test
    void thenApply() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture
                .thenApply(saidMessage -> "applied message : " + saidMessage)
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.info("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.handle()")
    @Test
    void handle() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message), threadPoolTaskExecutor);

        /* when */
        String result = messageFuture
                .handle((saidMessage, throwable) -> {
                    log.info("handle : CompletableFuture가 처음 진행한 스레드가 쭉 이어서 진행한다.");
                    return "applied message : " + saidMessage;
                })
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.info("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.handleAsync()")
    @Test
    void handleAsync() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message), threadPoolTaskExecutor);

        /* when */
        String result = messageFuture
                .handleAsync((saidMessage, throwable) -> {
                    log.info("handleAsync : 스레드 풀을 지정하지 않으면 기본 스레드 풀의 새로운 스레드가 async하게 진행한다.");
                    return "applied message : " + saidMessage;
                })
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.info("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.handleAsync() with thread pool")
    @Test
    void handleAsyncWithThreadPool() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message), threadPoolTaskExecutor);

        /* when */
        String result = messageFuture
                .handleAsync((saidMessage, throwable) -> {
                    log.info("handleAsync with Thread Pool : 지정한 스레드 풀의 새로운 스레드가 async하게 진행한다.");
                    return "applied message : " + saidMessage;
                }, threadPoolTaskExecutor)
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.info("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.thenCompose()")
    @Test
    void thenCompose() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture
                .thenApply(saidMessage -> "applied message : " + saidMessage)
                .thenCompose(appliedMessage -> {
                    log.info("thenCompose : {}", appliedMessage);
                    return CompletableFuture.completedFuture(appliedMessage);
                })
                .join();

        /* then */
        log.info("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    private String sayMessage(String message) {
        sleepOneSecond();

        return "say " + message;
    }

    private void sleepOneSecond() {
        try {
            log.info("start to sleep 1 second.");
            Thread.sleep(1000);
            log.info("end to sleep 1 second.");
        } catch (InterruptedException e) {
            throw new IllegalStateException();
        }
    }

}

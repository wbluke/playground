package com.wbluke.playground.learning.completablefuture;

import com.wbluke.playground.learning.SleepExecutor;
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
@SpringBootTest
public class CompletableFutureTest2 {

    @Autowired
    private Executor threadPoolTaskExecutor;

    @Autowired
    private SleepExecutor sleepExecutor;

    @DisplayName("CompletableFuture.allOf()")
    @Test
    void allOf() {
        /* given */
        List<String> messages = Arrays.asList("Hello", "Hi", "Bye", "Yes", "No");
        List<CompletableFuture<String>> messageFutures = messages.stream()
                .map(sleepExecutor::sayMessageAfterOneSecond)
                .collect(Collectors.toList());

        /* when */
        List<String> saidMessages = CompletableFuture.allOf(messageFutures.toArray(new CompletableFuture[0]))
                .thenApply(Void -> messageFutures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();

        /* then */
        for (String message : saidMessages) {
            System.out.println("message = " + message);
        }
    }

    @DisplayName("CompletableFuture.thenApply()")
    @Test
    void thenApply() {
        /* given */
        CompletableFuture<String> messageFuture = sleepExecutor.sayMessageAfterOneSecond("Hello");

        /* when */
        String result = messageFuture.thenApply(message -> "applied message : " + message)
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.debug("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.handle()")
    @Test
    void handle() {
        /* given */
        CompletableFuture<String> messageFuture = sleepExecutor.sayMessageAfterOneSecond("Hello");

        /* when */
        String result = messageFuture.handle(
                (message, throwable) -> {
                    log.debug("handle : CompletableFuture가 처음 진행한 스레드가 쭉 이어서 진행한다.");
                    return "applied message : " + message;
                })
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.debug("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.handleAsync()")
    @Test
    void handleAsync() {
        /* given */
        CompletableFuture<String> messageFuture = sleepExecutor.sayMessageAfterOneSecond("Hello");

        /* when */
        String result = messageFuture.handleAsync(
                (message, throwable) -> {
                    log.debug("handleAsync : 스레드 풀을 지정하지 않으면 기본 스레드 풀의 새로운 스레드가 async하게 진행한다.");
                    return "applied message : " + message;
                })
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.debug("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.handleAsync() with thread pool")
    @Test
    void handleAsyncWithThreadPool() {
        /* given */
        CompletableFuture<String> messageFuture = sleepExecutor.sayMessageAfterOneSecond("Hello");

        /* when */
        String result = messageFuture.handleAsync(
                (message, throwable) -> {
                    log.debug("handleAsync with Thread Pool : 지정한 스레드 풀의 새로운 스레드가 async하게 진행한다.");
                    return "applied message : " + message;
                }, threadPoolTaskExecutor)
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture) // 비동기 동작을 확인하기 위해 Blocking
                .join();

        log.debug("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

    @DisplayName("CompletableFuture.thenCompose()")
    @Test
    void thenCompose() {
        /* given */
        CompletableFuture<String> messageFuture = sleepExecutor.sayMessageAfterOneSecond("Hello");

        /* when */
        String result = messageFuture.thenApply(message -> "applied message : " + message)
                .thenCompose(message -> {
                    log.debug("thenCompose : {}", message);
                    return CompletableFuture.completedFuture(message);
                })
                .join();

        /* then */
        log.debug("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

}

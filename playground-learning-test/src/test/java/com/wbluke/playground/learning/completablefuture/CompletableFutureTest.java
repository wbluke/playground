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
@SpringBootTest
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
        assertThat(result).isEqualTo("Say Hello");
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
        assertThat(result).isEqualTo("Hello");
    }

    @DisplayName("CompletableFuture.thenApply()")
    @Test
    void thenApply() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture
                .thenApply(saidMessage -> "Applied message : " + saidMessage)
                .join();

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    @DisplayName("CompletableFuture.thenAccept()")
    @Test
    void thenAccept() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */ /* then */
        messageFuture
                .thenAccept(saidMessage -> {
                    String acceptedMessage = "accepted message : " + saidMessage;
                    log.info("thenAccept {}", acceptedMessage);
                })
                .join();
    }

    @DisplayName("thenApply() : 처음 진행한 스레드가 쭉 이어서 진행한다.")
    @Test
    void thenApplyWithSameThread() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(
                () -> sayMessage(message), threadPoolTaskExecutor
        );

        /* when */
        String result = messageFuture
                .thenApply(saidMessage -> {
                    log.info("thenApply() : Same Thread");
                    return "Applied message : " + saidMessage;
                })
                .join();

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    @DisplayName("thenApplyAsync() : 스레드 풀을 지정하지 않으면 기본 스레드 풀의 새로운 스레드가 async하게 진행한다.")
    @Test
    void thenApplyAsync() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(
                () -> sayMessage(message), threadPoolTaskExecutor
        );

        /* when */
        String result = messageFuture
                .thenApplyAsync(saidMessage -> {
                    log.info("thenApplyAsync() : New thread in another thread pool");
                    return "Applied message : " + saidMessage;
                })
                .join();

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    @DisplayName("handleAsync() : 지정한 스레드 풀의 새로운 스레드가 async하게 진행한다.")
    @Test
    void thenApplyAsyncWithAnotherThreadPool() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(
                () -> sayMessage(message), threadPoolTaskExecutor
        );

        /* when */
        String result = messageFuture
                .thenApplyAsync(saidMessage -> {
                    log.info("thenApplyAsync() : New thread in same thread pool");
                    return "Applied message : " + saidMessage;
                }, threadPoolTaskExecutor)
                .join();

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
    }

    @DisplayName("CompletableFuture.exceptionally()")
    @Test
    void exceptionally() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessageThrowingException(message));

        /* when */
        String result = messageFuture
                .exceptionally(Throwable::getMessage)
                .join();

        /* then */
        assertThat(result).isEqualTo("java.lang.IllegalStateException: exception message");
    }

    @DisplayName("CompletableFuture.handle()")
    @Test
    void handle() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture
                .handle((saidMessage, throwable) -> "Applied message : " + saidMessage)
                .join();

        /* then */
        assertThat(result).isEqualTo("Applied message : Say Hello");
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
        List<String> expectedMessages = Arrays.asList("Say Hello", "Say Hi", "Say Bye", "Say Yes", "Say No");
        assertThat(expectedMessages.equals(saidMessages)).isTrue();
    }

    @DisplayName("CompletableFuture.thenCompose()")
    @Test
    void thenCompose() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture
                .thenCompose(saidMessage -> CompletableFuture.supplyAsync(() -> {
                    sleepOneSecond();
                    return saidMessage + "!";
                }))
                .join();

        /* then */
        assertThat(result).isEqualTo("Say Hello!");
    }

    @DisplayName("CompletableFuture.thenCombine()")
    @Test
    void thenCombine() {
        /* given */
        String message = "Hello";
        CompletableFuture<String> messageFuture = CompletableFuture.supplyAsync(() -> sayMessage(message));

        /* when */
        String result = messageFuture
                .thenCombine(CompletableFuture.supplyAsync(() -> {
                    sleepOneSecond();
                    return "!";
                }), (message1, message2) -> message1 + message2)
                .join();

        /* then */
        assertThat(result).isEqualTo("Say Hello!");
    }

    private String sayMessage(String message) {
        sleepOneSecond();

        String saidMessage = "Say " + message;
        log.info("Said Message = {}", saidMessage);

        return saidMessage;
    }

    private String sayMessageThrowingException(String message) {
        sleepOneSecond();

        throw new IllegalStateException("exception message");
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

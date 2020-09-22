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
public class CompletableFutureTest {

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
        CompletableFuture.allOf(messageFuture)
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
        String result = messageFuture.handle((message, throwable) -> "applied message : " + message)
                .join();

        /* then */
        CompletableFuture.allOf(messageFuture)
                .join();

        log.debug("result = {}", result);
        assertThat(result).isEqualTo("applied message : say Hello");
    }

}

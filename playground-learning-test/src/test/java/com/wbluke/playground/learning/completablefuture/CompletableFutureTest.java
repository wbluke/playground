package com.wbluke.playground.learning.completablefuture;

import com.wbluke.playground.learning.SleepExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@SpringBootTest
public class CompletableFutureTest {

    @Autowired
    private SleepExecutor sleepExecutor;

    @DisplayName("CompletableFuture")
    @Test
    void name() {
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

}

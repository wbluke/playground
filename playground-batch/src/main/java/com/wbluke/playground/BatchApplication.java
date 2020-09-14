package com.wbluke.playground;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@EnableBatchProcessing
@SpringBootApplication
public class BatchApplication {

    public static void main(String[] args) {
        int exit = SpringApplication.exit(SpringApplication.run(BatchApplication.class, args));
        log.info("exitCode={}", exit);
        System.exit(exit);
    }

}

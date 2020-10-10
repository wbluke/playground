package com.wbluke.playground.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoggingController {

    @Value("${logging-module.version}")
    private String version;

    @GetMapping("/")
    public String version() {
        return String.format("Project Version : %s", version);
    }

    @GetMapping("/health")
    public String checkHealth() {
        return "healthy";
    }

    @GetMapping("/log/error")
    public String logError() {
        log.error("log error");
        return "error";
    }

    @GetMapping("/log/warn")
    public String logWarn() {
        log.warn("log warn");
        return "warn";
    }

    @GetMapping("/log/info")
    public String logInfo() {
        log.info("log info");
        return "info";
    }

    @GetMapping("/log/debug")
    public String logDebug() {
        log.debug("log debug");
        return "debug";
    }

}

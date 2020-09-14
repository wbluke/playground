package com.wbluke.playground.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class SimpleScopeCheckBatch {

    private static final String JOB_NAME = "SimpleScopeCheckBatch";
    private static final String BEAN_PREFIX = JOB_NAME + "_";

    @Value("${chunkSize:1000}")
    private int chunkSize;

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean(BEAN_PREFIX + "job")
    public Job simpleScopeCheckJob() {
        return jobBuilderFactory.get(JOB_NAME)
//                .start(simpleStep())
//                .next(simpleTaskletStep())
                .start(simpleTaskletStep())
                .build();
    }

    @Bean(BEAN_PREFIX + "simpleStep")
    @JobScope
    public Step simpleStep() {
        return stepBuilderFactory.get("simpleStep")
                .chunk(chunkSize)
                .reader(simpleReader())
                .writer(simpleWriter())
                .build();
    }

    @Bean(BEAN_PREFIX + "simpleReader")
    @StepScope
    public ItemReader<Object> simpleReader() {
        log.info("Simple Reader Start");

        return Object::new;
    }

    @Bean(BEAN_PREFIX + "simpleWriter")
    @StepScope
    public ItemWriter<Object> simpleWriter() {
        log.info("Simple Writer Start");

        return items -> {

        };
    }

    @Bean(BEAN_PREFIX + "simpleTaskletStep")
    @JobScope
    public Step simpleTaskletStep() {
        return stepBuilderFactory.get("simpleTaskletStep")
                .tasklet((contribution, chunkContext) -> {
                    log.info("Simple Tasklet Start");

                    return RepeatStatus.FINISHED;
                }).build();
    }

}

package com.wbluke.playground.learning.lombok;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ExampleBeanConsumerTest {

    @Autowired
    private ExampleBeanConsumer exampleBeanConsumer;

    @DisplayName("lombok + @Qualifier 테스트")
    @Test
    void exampleBeanConsumer() {
        assertThat(exampleBeanConsumer.matchA("A")).isTrue();
        assertThat(exampleBeanConsumer.matchB("B")).isTrue();
    }
}

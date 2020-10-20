package com.wbluke.playground.learning.config;

import com.wbluke.playground.learning.lombok.ExampleBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LombokTestConfig {

    public static final String EXAMPLE_BEAN_A_NAME = "exampleA";
    public static final String EXAMPLE_BEAN_B_NAME = "exampleB";

    @Bean
    @Qualifier(EXAMPLE_BEAN_A_NAME)
    public ExampleBean exampleBeanA() {
        return new ExampleBean("A");
    }

    @Bean
    @Qualifier(EXAMPLE_BEAN_B_NAME)
    public ExampleBean exampleBeanB() {
        return new ExampleBean("B");
    }

}

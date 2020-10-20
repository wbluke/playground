package com.wbluke.playground.learning.lombok;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import static com.wbluke.playground.learning.config.LombokTestConfig.EXAMPLE_BEAN_A_NAME;
import static com.wbluke.playground.learning.config.LombokTestConfig.EXAMPLE_BEAN_B_NAME;

@Getter
@RequiredArgsConstructor
@Component
public class ExampleBeanConsumer {

    @Qualifier(EXAMPLE_BEAN_A_NAME)
    private final ExampleBean exampleBean1;

    @Qualifier(EXAMPLE_BEAN_B_NAME)
    private final ExampleBean exampleBean2;

    public boolean matchA(String text) {
        return exampleBean1.match(text);
    }

    public boolean matchB(String text) {
        return exampleBean2.match(text);
    }

}

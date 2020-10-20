package com.wbluke.playground.learning.lombok;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ExampleBean {

    private final String text;

    public boolean match(String text) {
        return this.text.equals(text);
    }

}

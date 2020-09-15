package com.wbluke.playground.learning;

import com.wbluke.playground.core.domain.article.Article;
import com.wbluke.playground.core.domain.article.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CompletableFutureTest {

    @Autowired
    private ArticleRepository articleRepository;

    @DisplayName("CompletableFuture")
    @Test
    void name() {
        /* given */
        Article article = createArticle("title1", "contents1");
        articleRepository.save(article);

        /* when */

        /* then */
    }

    private Article createArticle(String title, String contents) {
        return Article.builder()
                .title(title)
                .contents(contents)
                .build();
    }

}

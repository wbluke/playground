package playground.logging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class LoggingControllerTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void test() {
        /* then */
        assertDoesNotThrow(() -> applicationContext.getBean(LoggingController.class));
    }

}

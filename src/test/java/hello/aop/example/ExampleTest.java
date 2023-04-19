package hello.aop.example;

import hello.aop.example.aop.RetryAspect;
import hello.aop.example.aop.TraceAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
//@Import(TraceAspect.class)
@Import({TraceAspect.class, RetryAspect.class})
public class ExampleTest {

    @Autowired
    ExampleService exampleService;

    @Test
    void test() {
        for (int i = 0; i < 5; i++) {
            log.info("client request i = {}", i);
            exampleService.request("data" + i);
        }
    }
}

package hello.aop.example;

import hello.aop.example.aop.NullExceptionAspect;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@SpringBootTest
@Import(NullExceptionAspect.class)
public class NullAnnotationTest {
    @Autowired
    NullAnnotationService nullAnnotationService;

    @Test
    void exceptionIfNullTest() {
        RuntimeException runtimeException = Assertions.assertThrows(RuntimeException.class, () -> nullAnnotationService.exceptionIfNullTest(null));
        log.info("error message = {}", runtimeException.getMessage());
    }

    @Test
    void returnIfNullTest() {
        String result = nullAnnotationService.returnIfNullTest(null);
        log.info("result={}", result);
    }

    @Test void success() {
        String s = nullAnnotationService.returnIfNullTest("exist param #1");
        log.info("returnIfNullTest() >> result = {}", s);
        String s1 = nullAnnotationService.exceptionIfNullTest("exist param #2");
        log.info("exceptionIfNullTest() >> result = {}", s1);
    }
}

package hello.aop.example.aop;

import hello.aop.example.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class RetryAspect {

    @Around("@annotation(retry)")
    public Object doRetry(ProceedingJoinPoint joinPoint, Retry retry) throws Throwable {
        int max = retry.value();
        Exception exception = null;
        for (int retryCount = 1; retryCount < max; retryCount++) {
            try {
                log.info("[retry] {} try count = {}/{}", joinPoint.getSignature(), retryCount, max);
                return joinPoint.proceed();
            } catch (Exception e) {
                exception = e;
            }
        }
        throw exception;
    }
}

package hello.aop.example.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class TraceAspect {

    @Before("@annotation(hello.aop.example.annotation.Trace)")
    public void doTrace(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        log.info("[trace] {} args = {}", joinpoint.getSignature(), args);
    }
}

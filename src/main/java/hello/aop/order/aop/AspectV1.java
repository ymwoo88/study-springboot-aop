package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Slf4j
@Aspect
public class AspectV1 {

    @Around("execution(* hello.aop.order..*(..))")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        /*
            스프링 AOP는 AspectJ의 문법을 차용하고, 프록시 방식의 AOP를 제공한다. AspectJ를 직접 사용하는 것이 아니다.
            스프링 AOP를 사용할 때는 @Aspect 어노테이션을 주로 사용하는데, 이 어노테이션도 AspectJ가 제공하는 어노테이션이다.
            import org.aspectj.lang.annotation.Around;
            import org.aspectj.lang.annotation.Aspect;
         */
        log.info("[log] {}", joinPoint.getSignature()); // join point 시그니쳐
        return joinPoint.proceed();
    }
}

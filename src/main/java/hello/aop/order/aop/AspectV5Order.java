package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

@Slf4j
public class AspectV5Order {
    /*
        ### @Aspect의 @Order 적용은 class레벨에서 적용이 가능하다!! (매우중요)
        - 다른형태의 @Around별로 class로 분리하여 @Aspect와 @Order를 지정한다.
        - 로그형태로 보았을 때 Service 진입 시점에 로그가 먼저찍히고!, 그 다음 Repository찍히고 outbound로 나갈 때의 순서로 찍힌다. 그림으로 쉽게 이해하겠지만 잘 생각해보아라 들어온데로 처리하고 나가는데로 처리한다.
     */
    @Aspect
    @Order(2)
    public static class LogAspect {
        @Around("hello.aop.order.aop.Pointcuts.allOrder()")
        @Order(2)
        public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
            log.info("[log] {}", joinPoint.getSignature()); // join point 시그니쳐
            return joinPoint.proceed();
        }
    }

    @Aspect
    @Order(1)
    public static class TransactionAspect {
        // hello.aop.order 패키지와 하위 패키지 이면서 클래스 이름 패턴이 *Service 인것!
        @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
        @Order(1)
        public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
            try {
                log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
                Object result = joinPoint.proceed();
                log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
                return result;
            } catch (Exception e) {
                log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
                throw e;
            } finally {
                log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
            }
        }
    }
}

package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV4Pointcut {

    @Around("hello.aop.order.aop.Pointcuts.allOrder()")
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

    // hello.aop.order 패키지와 하위 패키지 이면서 클래스 이름 패턴이 *Service 인것!
    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
        /*
            - allOrder() 포인트컷은 hello.aop.order 패키지와 하위 패키지를 대상으로 한다.
            - allService() 포인트컷은 타입 이름 패턴이 *Service 를 대상으로 하는데 쉽게 이야기해서 xxxServicec처럼 service로 끝나는 명을본다 앞뒤로 * 사용도 가능하다
            - 여기서 타입 이름 패턴이라고 한 이유는 클래스, 인터페이스에 모두 적용되기 때문이다.

            ### @Around("allOrder() && allService()"
            - 포인트컷은 이렇게 조합할 수 있다 &&, ||, ! 3가지 조합이 가능하다
            - 결과적으로 doTransaction() 어드바이스는 현재 강의 내용에서는 OrderService에만 적용된다.
            - doLog() 는 order하위 패키지 모두에 적용 된다.

            ### 포이늩컷이 적용 된 AOP결과는 다음과 같다.
            - orderService : doLog(), doTransaction() 어드바이스 적용
            - orderRepository : doLog() 어드바이스 적용
         */
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

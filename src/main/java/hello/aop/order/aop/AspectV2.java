package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
@Aspect
public class AspectV2 {

    // hello.aop.order 패키지와 하위 패키지 지정
    @Pointcut("execution(* hello.aop.order..*(..))")
    private void allOrder() {
        /*
            @pointcut
               메소드 이름과 파라미터를 합쳐서 포인트컷 시그니쳐라 한다.
               메소드의 반환 타입은 void이여야 한다.
               코드 내용은 비워둔다.
               포인트컷 시그니쳐는 allOrder() 이다. 이름 그대로 주문과 관련된 모든 기능을 대상으로 하는 포인트컷이다.
               @Around 어드바이스에서는 포인트컷을 직접 지정해도 되지만, 포인트컷 시그니쳐를 사용해도 된다. 여기서는 @Around("allOrder()")를 사용한다.
               private, public 같은 접근제어자는 내부에서만 사용하면 private를 사용해도 되지만, 다른 에스펙트에서 참고하려면 public을 사용해야한다.

               결과적으로 AspectV1 과 같은 기능을 수행한다. 이렇게 분리하면 하나의 포인트컷 표현식을 여러 어드바이스에서 함께 사용할 수 있다.
         */
    } // pointcut 시그니쳐

    @Around("allOrder()")
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

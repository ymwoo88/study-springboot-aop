package hello.aop.order.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Slf4j
@Aspect
public class AspectV6Advice {

//    @Around("hello.aop.order.aop.Pointcuts.orderAndService()")
//    public Object doTransaction(ProceedingJoinPoint joinPoint) throws Throwable {
//        try {
//            // @Bofore
//            log.info("[트랜잭션 시작] {}", joinPoint.getSignature());
//            Object result = joinPoint.proceed();
//            // @AfterReturning
//            log.info("[트랜잭션 커밋] {}", joinPoint.getSignature());
//            return result;
//        } catch (Exception e) {
//            // @AfterThrowing
//            log.info("[트랜잭션 롤백] {}", joinPoint.getSignature());
//            throw e;
//        } finally {
//            // @After
//            log.info("[리소스 릴리즈] {}", joinPoint.getSignature());
//        }
//    }

    @Before("hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doBefore(JoinPoint joinPoint) {
        log.info("[before] {}", joinPoint.getSignature());
    }

    /*
        return 은 조작불가능 바꿀수 없다
     */
    @AfterReturning(value = "hello.aop.order.aop.Pointcuts.orderAndService()", returning = "result")
    public void doReturn(JoinPoint joinPoint, Object result) {
        log.info("[return] {} return={}", joinPoint.getSignature(), result);
    }

    @AfterThrowing(value = "hello.aop.order.aop.Pointcuts.orderAndService()", throwing = "ex")
    public void doThrowing(JoinPoint joinPoint, Exception ex) {
        log.info("[Throwing] {} ex={}", joinPoint.getSignature(), ex);
    }

    @After(value = "hello.aop.order.aop.Pointcuts.orderAndService()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("[after] {} ", joinPoint.getSignature());
    }

    /*
        복잡해보이지만 사실 @Around를 제외한 나머지 어드바이스들은 @Around가 할 수 있는 기능 일부분을 수행한다.
        따라서 @Around로도 충분히 소화는 가능하다

        @Before
            @Around를 사용할 경우 ProceedingJoinPoint.proceed()를 꼭 호출해야 다음대상이 호출된다.
            만약 호출하지 않으면? 다음 로직이 수행되지 않기때문에 장애가 발생할 수 있다.
            @Before의 경우는 AOP에 위임되어 자동으로 다음 로직이 수행된다. 인적실수면에서 유용할 거 같다.
            단 예외가 발생하면 다음 로직은 수행되지 않는다.
        @AfterReturning
            메서드 실행이 정상적으로 반환될 때 실행
            returning 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야한다.
            returning 절에 지정된 타입의 값을 반환하는 메서드만 대상으로 실행한다. (부모 타입을 지정하면 모든 자식 타입은 인정된다.)
            @Around와 다르게 반환되는 객체를 변경할 수는 없다. 반환 객체를 변경하려면 @Around를 사용해야한다.
        @AfterThrowing
            메서드 실행이 예외를 던져서 종료될 때 실행
            Throwing 속성에 사용된 이름은 어드바이스 메서드의 매개변수 이름과 일치해야 한다.
            Throwing 절에 지정된 타입과 맞은 예외를 대상으로 실행한다. (부모 타입을 지정하면 모듬 자식 타입은 인정된다.)
        @After
            메서드 실행이 종료되면 실행한다. (finally라고 생각하면된다.)
            정상 및 예외 반환 조건을 모두 처리한다.
            일반적으로 리소스 및 유사한 목적을 해제하는 데 사용한다.
        @Around
            메서드의 실행의 주변에서 실행된다. 메서드 실행 전후에 작업을 주행한다.
            가장 강력한 어드바이스이기도 하다
                조인 포인트 실행 여부 선택 joinPoint.proceed() 호출 여부 선택
                전달 값 반환 : joinPoint.proceed(args[])
                반환 값 변환
                예외 변환
                트랜잭션 처럼 try catch finally 모두 들어가는 구문 처리 가능
            어드바이스의 첫 번째 파라미터는 ProceedingJoinPoint를 사용해야한다.
            proceed() 를 통해 대상을 실행한다.
            proceed()를 여러번 실행할 수도 있다.

     */
}


package hello.aop.order.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    // hello.aop.order 하위 패키지 모두
    @Pointcut("execution(* hello.aop.order..*(..))")
    public void allOrder() {}

    // Service 명으로 된 클래스 모두
    @Pointcut("execution(* *..*Service.*(..))")
    public void allService() {}

    // hello.aop.order 모두와 Service로 시작되는 패턴
    @Pointcut("allOrder() && allService()")
    public void orderAndService() {}
}

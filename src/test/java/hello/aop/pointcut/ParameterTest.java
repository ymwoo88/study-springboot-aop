package hello.aop.pointcut;

import hello.aop.member.MemberService;
import hello.aop.member.annotation.ClassAop;
import hello.aop.member.annotation.MethodAop;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Slf4j
@Import(ParameterTest.ParameterAspect.class)
@SpringBootTest
public class ParameterTest {

    @Autowired
    MemberService memberService;

    @Test
    void success() {
        log.info("memberService Proxy={}", memberService.getClass());
        memberService.hello("helloA");
    }

    @Slf4j
    @Aspect
    static class ParameterAspect {

        @Pointcut("execution(* hello.aop.member..*.*(..))")
        private void allMember() {
        }

        /**
         * ProceedingJoinPoint에서 필요한것을 모두 사용
         */
        @Around("allMember()")
        public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
            Object args1 = joinPoint.getArgs()[0];
            log.info("[logArgs1]{}, arg={}", joinPoint.getSignature(), args1);
            return joinPoint.proceed();
        }

        /**
         * args를 사용하면 parameter에서 바로 받아서 사용할 수 있다.
         */
        @Around("allMember() && args(arg, ..)")
        public Object logArgs2(ProceedingJoinPoint joinPoint, Object arg) throws Throwable {
            log.info("[logArgs2]{}, arg={}", joinPoint.getSignature(), arg);
            return joinPoint.proceed();
        }

        /**
         * Befores는 핵심이아니나 좀더 간단하게 값을 받아 사용할 수 있다.
         */
        @Before("allMember() && args(arg, ..)")
        public void logArgs3(String arg) {
            log.info("[logArgs3], arg={}", arg);
        }

        /**
         * - this, target 비슷한애들이다.
         * - object 타입으로 선언하여 parameter에서 사용할 수 있지만 둘에 큰 차이점은 직접객체냐, 프록시객체냐 차이가 존재한다.
         * 2023-03-29 19:56:42.731  INFO 10265 --- [    Test worker] h.a.p.ParameterTest$ParameterAspect      : [targetArgs]String hello.aop.member.MemberServiceImpl.hello(String), obj=class hello.aop.member.MemberServiceImpl
         * 2023-03-29 19:56:42.731  INFO 10265 --- [    Test worker] h.a.p.ParameterTest$ParameterAspect      : [thisArgs]String hello.aop.member.MemberServiceImpl.hello(String), obj=class hello.aop.member.MemberServiceImpl$$EnhancerBySpringCGLIB$$38b3c3e4
         *
         * 정리
         * - this : 프록시 객체를 전달 받는다
         * - target : 실제 대상 객체를 전달 받는다 (프록시 이전 오리지날 객체)
         * - @target, @within : 타입의 어노테이션을 전달 받는다.
         * - @Annotation : 메서드의 어노테이션을 전달 받는다. 여기서는 "annotation.getValue()"로 해당 값을 출력하는 결과를 확인 할 수있다.
         */
        @Before("allMember() && this(obj)")
        public void thisArgs(JoinPoint joinPoint, MemberService obj) {
            log.info("[thisArgs]{}, obj={}", joinPoint.getSignature(), obj.getClass());
        }
        @Before("allMember() && target(obj)")
        public void targetArgs(JoinPoint joinpoint, MemberService obj) {
            log.info("[targetArgs]{}, obj={}", joinpoint.getSignature(), obj.getClass());
        }
        @Before("allMember() && @target(annotation)")
        public void atTarget(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@target]{}, annotation={}", joinPoint.getSignature(), annotation);
        }
        @Before("allMember() && @within(annotation)")
        public void atWithin(JoinPoint joinPoint, ClassAop annotation) {
            log.info("[@within]{}, annotation={}", joinPoint.getSignature(), annotation);
        }
        @Before("allMember() && @annotation(annotation)")
        public void atAnnotation(JoinPoint joinPoint, MethodAop annotation) {
            log.info("[@annotation]{}, annotation={}, annotation.getValue={}", joinPoint.getSignature(), annotation, annotation.value());
        }
    }
}

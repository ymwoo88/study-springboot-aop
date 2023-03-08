package hello.aop.pointcut;

import hello.aop.member.MemberServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method helloMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException {
        helloMethod = MemberServiceImpl.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod() {
        // public java.lang.String hello.aop.member.MemberServiceImpl.hello(java.lang.String)
        log.info("helloMethod={}", helloMethod);
    }

    @Test
    void exactMatch() {
        /*
            접근제어자 : public
            반환타입 : String
            선언타입 : hello.aop.member.MemberServiceImpl
            메서드 이름 : hello
            파라미터 : (String)
            예외?: 생략
         */
        pointcut.setExpression("execution(public String hello.aop.member.MemberServiceImpl.hello(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : 생략
            메서드 이름 : *
            파라미터 : (..)
            예외?: 생략

            * 은 아무 값이 들어와도 된다는 뜻이다.
            파라미터에서 ..은 파라미터의 타입과 파라미터 수가 상관없다는 뜻이다.

         */
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatch() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : 생략
            메서드 이름 : hello
            파라미터 : (..)
            예외?: 생략
         */
        pointcut.setExpression("execution(* hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void nameMatchStart() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : 생략
            메서드 이름 : *ll*
            파라미터 : (..)
            예외?: 생략
         */
        pointcut.setExpression("execution(* *ll*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatch() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : full name
            메서드 이름 : full name
            파라미터 : (..)
            예외?: 생략
         */
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.hello(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchStar() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : * 끝에 사용
            메서드 이름 : *
            파라미터 : (..)
            예외?: 생략
         */
        pointcut.setExpression("execution(* hello.aop.member.*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void packageMatchSub() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : * 끝에 사용
            메서드 이름 : *
            파라미터 : (..)
            예외?: 생략


            .. 은 하위 패키지를 포함 시킨다는 의미이다.
         */
        pointcut.setExpression("execution(* hello.aop..*.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }
}

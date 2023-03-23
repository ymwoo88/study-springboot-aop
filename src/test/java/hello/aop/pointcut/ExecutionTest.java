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

    @Test
    void typeExactMatch() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : * 끝에 사용
            메서드 이름 : *
            파라미터 : (..)
            예외?: 생략


            class 명으로 매칭이 가능하다
         */
        pointcut.setExpression("execution(* hello.aop.member.MemberServiceImpl.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void typeMatchSuperType() {
        /*
            접근제어자 : 생략
            반환타입 : *
            선언타입 : * 끝에 사용
            메서드 이름 : *
            파라미터 : (..)
            예외?: 생략


            부모의 타입으로도 매칭이 가능하다
            excution 에서는 MemberService 처럼 부모 타입을 선언해도 그 자식 타입은 매칭된다. 다형성이 포용된다는 것을 알수 있다.
            단! 자식클래스에서 부모에 없는 메소드로는 부모타입으로는 매칭이 안된다.
            예를들어 인터페이스에 없고 자식클래스에만 있는 other() 이런 메소로 부모타입으로 매칭하면 매칭이 안될 것이다.
         */
        pointcut.setExpression("execution(* hello.aop.member.MemberService.*(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // String 타입의 파라미터를 허용
    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 파라미터가 없응 경우
    @Test
    void noArgsMatch() {
        pointcut.setExpression("execution(* *())");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isFalse();
    }

    // 정확히 하나의 파라미터만 허용, 대신 모든타입 허용
    @Test
    void argsMatchStart() {
        pointcut.setExpression("execution(* *(*))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // 모든 것을 허용
    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    // String이 맨처음으로오고 나머지는 모든것을 매칭
    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        Assertions.assertThat(pointcut.matches(helloMethod, MemberServiceImpl.class)).isTrue();
    }

    /*
        excution 파라미터 매칭 규칙은 다음과 같다.
            (스트링) 정확하게 스프링타입 파라미터
            () 파라미터가 없어야 한다.
            (*) 정확히 하나의 파라미터, 단 모든 타입을 허용한다.
            (*, *) 정확히 두 개의 파라미터, 단 모든 타입을 허용한다.
            (..) 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다. 참고로 파라미터가 없어도 된다. 0..*로 이해하면 된다.
            (스트링, ..) 스트링 타입으로 시작해야한다. 숫자와 무관하게 모든 파라미터, 모든 타입을 허용한다.
                예) (스트링, (스트링, xxx), (스트링, xxx, xxx) 허용
     */
}

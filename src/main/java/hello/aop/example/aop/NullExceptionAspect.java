package hello.aop.example.aop;

import hello.aop.example.annotation.ExceptionIfNull;
import hello.aop.example.annotation.ReturnIfNull;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;

@Slf4j
@Aspect
public class NullExceptionAspect {

    @Pointcut("execution(* *(.., @hello.aop.example.annotation.ReturnIfNull (*), ..))")
    private void returnIfNull() {
    }

    @Pointcut("execution(* *(.., @hello.aop.example.annotation.ExceptionIfNull (*), ..))")
    private void exceptionIfNull() {
    }

    @Around("returnIfNull() || exceptionIfNull()")
    public Object logArgs1(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            Annotation[] annotations = parameter.getAnnotations();
            for (Annotation annotation : annotations) {
                Object arg = args[i];
                boolean isNull = Objects.isNull(arg);
                if (annotation.annotationType() == ReturnIfNull.class && isNull) {
                    return null;
                } else if (annotation.annotationType() == ExceptionIfNull.class && isNull) {
                    throw new NullPointerException("{" + parameter.getName() + "]parameter is null.");
                }
            }
        }
        return joinPoint.proceed();
    }
}

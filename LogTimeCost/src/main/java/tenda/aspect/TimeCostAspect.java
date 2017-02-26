package tenda.aspect;

import tenda.annotation.LogTimeCost;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimeCostAspect {

    @Around("@annotation(tenda.annotation.LogTimeCost) && @annotation(annotation)")
    public Object around(ProceedingJoinPoint joinPoint, LogTimeCost annotation) throws Throwable {
        long start = annotation.value().now();
        Object result = null;
        result = joinPoint.proceed();
        long end = annotation.value().now();
        System.out.println(joinPoint.getTarget() + " " + joinPoint.getSignature().getName() + " cost " + annotation.value().name()+ ": " + (end-start));
        return result;
    }

}

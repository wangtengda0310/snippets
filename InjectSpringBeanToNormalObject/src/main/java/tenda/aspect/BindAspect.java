package tenda.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import tenda.annotation.BindSpring;

import java.lang.reflect.Field;

@Aspect
@Component
public class BindAspect implements ApplicationContextAware {
    private ApplicationContext context;

    @Around("@annotation(tenda.annotation.BindSpring) && @annotation(annotation)")
    public Object after(ProceedingJoinPoint joinPoint, BindSpring annotation) throws Throwable {
        Object result = joinPoint.proceed();
        for (Field field : result.getClass().getFields()) {
            Class<?> type = field.getType();
            if (type.isPrimitive()) {
                continue;
            }
            Object bean = context.getBean(type);
            if (bean != null) {
                field.set(result, bean);
            }
        }
        return result;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}

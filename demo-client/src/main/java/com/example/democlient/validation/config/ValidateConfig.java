package com.example.democlient.validation.config;

import com.example.democlient.validation.utils.Validatable;
import com.example.democlient.validation.utils.ValidationException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import java.util.Arrays;

@Component
@Aspect
public class ValidateConfig {
    @Before("execution(public * com.example.democlient.validation.service.*.*(..))")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Validatable.ValidateResult validateResult = Validatable.ValidateResult.newInstance();
        Arrays.stream(args)
              .filter(each -> each instanceof Validatable)
              .map(each -> (Validatable) each)
              .forEach(each -> {
                  each.validate(validateResult);
              });

        if (validateResult.hasError()) {
            throw new ValidationException(validateResult);
        }
    }
}

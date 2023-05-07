package com.roaker.notes.commons.idempotent.keyresolver.impl;

import cn.hutool.core.util.ArrayUtil;
import com.roaker.notes.commons.idempotent.annotation.Idempotent;
import com.roaker.notes.commons.idempotent.keyresolver.IdempotentKeyResolver;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * @author lei.rao
 * @since 1.0
 */
public class ExpressionIdempotentKeyResolver implements IdempotentKeyResolver {
    private final ParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
    private final ExpressionParser expressionParser = new SpelExpressionParser();

    @Override
    public String resolver(JoinPoint joinPoint, Idempotent idempotent) {
        Method method = getMethod(joinPoint);
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = this.parameterNameDiscoverer.getParameterNames(method);
        StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext();
        if (ArrayUtil.isNotEmpty(parameterNames)) {
            for (int i = 0; i < parameterNames.length; i++) {
                standardEvaluationContext.setVariable(parameterNames[i], args[i]);
            }
        }
        Expression expression = expressionParser.parseExpression(idempotent.keyArg());
        return expression.getValue(standardEvaluationContext, String.class);
    }

    private static Method getMethod(JoinPoint point) {
        // 处理，声明在类上的情况
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        if (!method.getDeclaringClass().isInterface()) {
            return method;
        }

        // 处理，声明在接口上的情况
        try {
            return point.getTarget().getClass().getDeclaredMethod(
                    point.getSignature().getName(), method.getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}

package lib.distlock.utils;

import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import java.lang.reflect.Method;

public class CustomSpringELParser {

    private static final ExpressionParser parser = new SpelExpressionParser();
    private static final ParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    public static Object getDynamicValue(String key, Method method, Object[] args, Object target){
        EvaluationContext context = new MethodBasedEvaluationContext(
            target, method, args, discoverer
        );

        return parser.parseExpression(key).getValue(context, Object.class);
    }
}

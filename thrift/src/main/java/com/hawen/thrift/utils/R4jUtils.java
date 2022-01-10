package com.hawen.thrift.utils;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class R4jUtils {
    //断路器注册机
    private static CircuitBreakerRegistry registry = null;

    //返回断路器注册机
    public static CircuitBreakerRegistry circuitBreakerRegistry(){
        if (null == registry) {
            CircuitBreakerConfig config = CircuitBreakerConfig.ofDefaults();
            registry = CircuitBreakerRegistry.of(config);
        }
        return registry;
    }
}

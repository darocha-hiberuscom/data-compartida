package com.pichincha.sp.infrastructure.exception.config;

import com.pichincha.sp.application.exception.BusinessException;
import com.pichincha.sp.application.exception.BancsIntegrationException;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.exception.error.resolver.ErrorResolver;
import com.pichincha.sp.infrastructure.exception.error.resolver.BusinessExceptionResolver;
import com.pichincha.sp.infrastructure.exception.error.resolver.BancsIntegrationExceptionResolver;
import com.pichincha.sp.infrastructure.exception.error.resolver.GlobalErrorExceptionResolver;
import com.pichincha.sp.infrastructure.exception.error.resolver.ResponseStatusExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ExceptionHandlerConfiguration {

    @Bean
    public Map<Class<?>, ErrorResolver<?>> errorResolverMap(
            BusinessExceptionResolver businessExceptionResolver,
            BancsIntegrationExceptionResolver bancsIntegrationExceptionResolver,
            GlobalErrorExceptionResolver globalErrorExceptionResolver,
            ResponseStatusExceptionResolver responseStatusExceptionResolver) {

        Map<Class<?>, ErrorResolver<?>> resolverMap = new HashMap<>();

        resolverMap.put(BusinessException.class, businessExceptionResolver);
        resolverMap.put(BancsIntegrationException.class, bancsIntegrationExceptionResolver);
        resolverMap.put(GlobalErrorException.class, globalErrorExceptionResolver);
        resolverMap.put(ResponseStatusException.class, responseStatusExceptionResolver);

        return resolverMap;
    }
}


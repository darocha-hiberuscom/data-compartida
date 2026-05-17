package com.pichincha.sp.infrastructure.exception.error.resolver;

import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultDto;
import com.pichincha.sp.infrastructure.input.adapter.rest.dto.SoapFaultEnvelopeDto;
import lombok.Getter;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ServerWebExchange;
import java.util.function.BiFunction;

@Getter
public abstract sealed class ErrorResolver<T extends Throwable>
        implements BiFunction<ServerWebExchange, T, SoapFaultEnvelopeDto>
        permits GlobalErrorExceptionResolver, ResponseStatusExceptionResolver, UnexpectedErrorResolver {

    protected abstract SoapFaultEnvelopeDto buildError(@NonNull ServerHttpResponse serverResponse,
                                                       @NonNull final String requestPath,
                                                       @NonNull final T throwable);

    @Override
    public SoapFaultEnvelopeDto apply(@NonNull final ServerWebExchange serverWebExchange,
                                      @NonNull final T throwable) {
        return buildError(serverWebExchange.getResponse(),
                serverWebExchange.getRequest().getPath().toString(), throwable);
    }
}

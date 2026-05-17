package com.pichincha.sp.infrastructure.exception.error.resolver.handler;

import com.pichincha.sp.infrastructure.exception.error.resolver.ErrorResolver;
import com.pichincha.sp.infrastructure.exception.error.resolver.UnexpectedErrorResolver;
import jakarta.validation.ConstraintViolationException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;
import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ErrorResolverHandler<T extends Throwable> implements ErrorWebExceptionHandler {

    private final Map<Class<T>, ErrorResolver<T>> resolverMap;
    private final UnexpectedErrorResolver unexpectedErrorResolver;

    @NonNull
    @Override
    public Mono<Void> handle(@NonNull ServerWebExchange serverWebExchange,
                             @NonNull Throwable throwable) {
        return Mono.just(serverWebExchange.getResponse())
                .doOnNext(response ->
                        response.getHeaders().setContentType(MediaType.APPLICATION_XML))
                .map(response -> createResponseAndResolverTuple(response, throwable))
                .flatMap(responseAndResolverTuple ->
                        writeResponse(responseAndResolverTuple, serverWebExchange, throwable));
    }

    private Tuple2<ServerHttpResponse, ErrorResolver<T>> createResponseAndResolverTuple(
            ServerHttpResponse response, Throwable throwable) {
        return Tuples.of(response,
                resolverMap.getOrDefault(throwable.getClass(),
                        getFallbackErrorResolver(throwable, ConstraintViolationException.class)));
    }

    @SafeVarargs
    @NonNull
    @SuppressWarnings({"unchecked"})
    private ErrorResolver<T> getFallbackErrorResolver(@NonNull final Throwable throwable,
                                                       @NonNull final Class<? extends Throwable>... classes) {
        return Stream.of(classes)
                .filter(theClass -> theClass.isInstance(throwable))
                .findFirst()
                .map(clazz -> resolverMap.getOrDefault(clazz, (ErrorResolver<T>) unexpectedErrorResolver))
                .orElse((ErrorResolver<T>) unexpectedErrorResolver);
    }

    @SuppressWarnings({"unchecked"})
    private Mono<Void> writeResponse(Tuple2<ServerHttpResponse, ErrorResolver<T>> responseAndResolverTuple,
                                      ServerWebExchange serverWebExchange, Throwable throwable) {
        return responseAndResolverTuple.getT1().writeWith(
                Mono.fromCallable(() -> {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    Object response = responseAndResolverTuple.getT2()
                            .apply(serverWebExchange, (T) throwable);
                    Class<?> responseClass = response.getClass();
                    JAXBContext context = JAXBContext.newInstance(responseClass);
                    Marshaller marshaller = context.createMarshaller();
                    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
                    marshaller.marshal(response, baos);
                    return baos.toByteArray();
                }).map(responseAndResolverTuple.getT1().bufferFactory()::wrap)
        );
    }
}

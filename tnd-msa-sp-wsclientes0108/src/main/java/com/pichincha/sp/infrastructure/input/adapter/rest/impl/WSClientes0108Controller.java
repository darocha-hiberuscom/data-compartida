package com.pichincha.sp.infrastructure.input.adapter.rest.impl;

import com.pichincha.sp.application.port.input.CreateCustomerInput;
import com.pichincha.sp.infrastructure.constants.SoapControllerConstants;
import com.pichincha.sp.infrastructure.exception.BusinessValidationException;
import com.pichincha.sp.infrastructure.exception.GlobalErrorException;
import com.pichincha.sp.infrastructure.input.adapter.rest.mapper.WsClientes0108SoapMapper;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.request.SoapBodyRequest;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.request.SoapEnvelopeRequest;
import com.pichincha.sp.infrastructure.input.adapter.soap.dto.response.SoapEnvelopeResponse;
import com.pichincha.sp.infrastructure.logging.CustomLogLevel;
import com.pichincha.sp.infrastructure.logging.CustomLogLevelHandler;
import com.pichincha.sp.infrastructure.logging.annotation.BpTraceable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(SoapControllerConstants.SOAP_SERVICE_PATH)
public class WSClientes0108Controller {

    private final CreateCustomerInput createCustomerInput;
    private final WsClientes0108SoapMapper wsClientes0108SoapMapper;
    private final CustomLogLevelHandler customLogLevelHandler;

    @BpTraceable
    @PostMapping(
        consumes = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE, "application/soap+xml"},
        produces = {MediaType.TEXT_XML_VALUE, MediaType.APPLICATION_XML_VALUE, "application/soap+xml"}
    )
    public Mono<SoapEnvelopeResponse> crearCliente24(@RequestBody @Valid SoapEnvelopeRequest request) {
        customLogLevelHandler.log(CustomLogLevel.INFO, Thread.currentThread().getStackTrace(),
                SoapControllerConstants.LOG_SOAP_REQUEST_START);

        SoapBodyRequest body = Optional.ofNullable(request)
                .map(SoapEnvelopeRequest::getBody)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        SoapControllerConstants.SOAP_BODY_NULL_MESSAGE));

        return routeToOperation(body, request)
                .onErrorResume(BusinessValidationException.class,
                        exception -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception)))
                .onErrorResume(GlobalErrorException.class,
                        exception -> Mono.error(new ResponseStatusException(exception.getStatusCode(), exception.getMessage(), exception)))
                .onErrorResume(Throwable.class,
                        exception -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), exception)))
                .doOnSuccess(result -> customLogLevelHandler.log(CustomLogLevel.INFO,
                        Thread.currentThread().getStackTrace(),
                        SoapControllerConstants.LOG_SOAP_REQUEST_END));
    }

    private Mono<SoapEnvelopeResponse> routeToOperation(SoapBodyRequest body, SoapEnvelopeRequest request) {
        return Optional.ofNullable(body.getCrearCliente24())
                .map(operation -> createCustomerInput.execute(soapDomainMapper.toDomain(request))
                        .map(domainSoapMapper::toSoapResponse))
                .orElseGet(() -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        SoapControllerConstants.NO_VALID_SOAP_OPERATION_MESSAGE)));
    }
}

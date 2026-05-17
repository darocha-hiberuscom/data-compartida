```markdown
# PROMPT BASE — MANEJO DE ERRORES TÉCNICOS EN ADAPTERS OUTPUT (WEBCLIENT)

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar el manejo de errores técnicos en adapters de salida que usan `WebClient` (WebFlux), garantizando trazabilidad y comportamiento consistente.

Cuando el error técnico llegue al servicio SOAP, la salida de error debe unificarse en estructura SOAP `500`, capturada y resuelta por `ErrorResolverHandler` + `ErrorResolver`.

## Reglas obligatorias
- Capturar y mapear errores técnicos de integración (HTTP, timeout, conectividad, serialización/deserialización).
- No propagar excepciones técnicas crudas sin contexto.
- Convertir errores técnicos a excepciones de infraestructura del proyecto.
- Preservar contrato reactivo (`Mono`) y manejo de errores reactivo (`onStatus`, `onErrorMap`, `timeout`).
- Aplicar el manejo sobre el bean `WebClient` específico del adapter (ej: `customerPaymentsWebClient`), no sobre `WebClient.Builder` genérico.
- En todos los métodos principales de adapters, usar `@EventAudit(service = "...", method = "...", type = AuditType.T)`.
- No usar SLF4J (`@Slf4j`, `LoggerFactory`, `log.*`).
- Anotar método del adapter con `@BpLogger`.
- Inyectar `private final CustomLogLevelHandler customLogLevelHandler;`.
- No definir estructuras SOAP de error alternativas a `SoapFaultEnvelopeDto` para la salida final del servicio SOAP.

## Tipos de error mínimos a cubrir
- HTTP 4xx
- HTTP 5xx
- Timeout de conexión/lectura
- Errores de red (`ConnectException`, `UnknownHostException`, etc.)
- Errores de parseo/payload (`DecodingException`, serialización)

## Trazabilidad obligatoria
Cada error técnico debe registrar, como mínimo:
- Servicio invocado
- Operación o adapter
- Endpoint objetivo
- Código/estado HTTP (si aplica)
- Mensaje técnico original

## Reglas de implementación
- Usar `retrieve().onStatus(...)` para mapear respuestas HTTP no exitosas.
- Aplicar `timeout(...)` en la cadena reactiva.
- Usar `onErrorMap(...)` para convertir excepciones técnicas a excepción de infraestructura.
- Inyectar `WebClient` por constructor y usar `@Qualifier("<cliente>WebClient")` cuando existan múltiples beans.
- Evitar lógica de negocio en el adapter; solo integración técnica.
- Mantener pipeline recomendado por operación: `executeRequest(...)` -> `doOnNext(...)` -> `onErrorMap(...)` -> `flatMap(validateResponse)`.
- Centralizar `mapError(...)` y `validateResponse(...)` por adapter para consistencia de manejo técnico/funcional.
- No duplicar resolución SOAP en adapters: la resolución final debe centralizarse en `ErrorResolverHandler`.
- Incluir `doOnError(...)` con el patrón obligatorio:

```java
.doOnError(error -> customLogLevelHandler.log(
  CustomLogLevel.ERROR,
  Thread.currentThread().getStackTrace(),
  error.getMessage() == null ? DomainValidationConstants.OPERATION_FLOW_ERROR : error.getMessage(),
  error.getMessage()))
```

## Convención sugerida de excepciones
- Excepción base técnica: `InfrastructureException` (o equivalente del proyecto)
- Excepciones específicas opcionales:
  - `ExternalService4xxException`
  - `ExternalService5xxException`
  - `ExternalServiceTimeoutException`
  - `ExternalServiceConnectivityException`

## Política de errores SOAP (obligatoria)
- Estructura de error final del SOAP: estado `500` + `SoapFaultEnvelopeDto` / `SoapFaultBodyDto` / `SoapFaultDto`.
- Punto de captura y resolución:
  - `infrastructure.exception.error.resolver.handler.ErrorResolverHandler`
  - `infrastructure.exception.error.resolver.ErrorResolver`
  - `GlobalErrorExceptionResolver`
  - `ResponseStatusExceptionResolver`
  - `UnexpectedErrorResolver`
- Tipos de error SOAP manejables:
  - `GlobalErrorException`
  - `ResponseStatusException`
  - `ConstraintViolationException`
  - `Throwable` no controlado (fallback a `UnexpectedErrorResolver`)

## Alcance
Aplicable a adapters en:
- `com.pichincha.sp.infrastructure.output.adapter`

## Referencia reutilizable
- Para incluir esta política en prompts nuevos:
  - `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER.md`

```
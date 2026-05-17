```markdown
# PROMPT BASE — MODELO DE EXCEPCIONES EN INPUT ADAPTER

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar el manejo de excepciones en `infrastructure.input.adapter.rest` acoplado al modelo legado del proyecto.

## Reglas obligatorias
- Reusar `com.pichincha.sp.domain.exception.BusinessException` como excepción funcional de negocio.
- Diferenciar explícitamente errores funcionales (`BusinessException`) de errores técnicos no controlados.
- Para errores funcionales, construir respuesta SOAP de error de negocio con presenter/wrapper.
- Para errores funcionales, tratar la respuesta como salida funcional correcta del contrato (no como fallo técnico del flujo).
- Para errores no controlados, responder fault genérico (`wrapGenericFault(buildGenericFault())`).
- No propagar excepciones crudas al cliente SOAP.
- No hardcodear códigos ni mensajes de error en el código del controller/service cuando deban ser configurables.
- Consumir códigos/mensajes de error desde propiedades (`application.yml`) mediante `@Value` o `@ConfigurationProperties`.
- `application.yml` debe referenciar variables de entorno y estas deben provenir de Helm configmap por ambiente.

## Patrón obligatorio (controller input)
- `validateSoapRequest(...)` retorna `Mono<Void>` y lanza `Mono.error(new BusinessException(...))` cuando valida header/body.
- `isBusinessException(Throwable)` para discriminar flujo de `onErrorResume`.
- `onErrorResume(...)` por operación:
  - `BusinessException` -> respuesta de error funcional por operación.
  - otro error -> fault genérico.
- Logging de inicio y error por operación con `CustomLogLevelHandler`, incluyendo el detalle de la `BusinessException`.
- En `onErrorResume(...)`, `BusinessException` debe resolverse con `Mono.just(respuesta funcional de negocio)`.
- Mantener pipeline reactivo por operación: `Mono.justOrEmpty(request)` -> validación -> mapeo request -> `portInput.execute(...)` -> wrap success -> `doOnError(...)` -> `onErrorResume(...)`.

## Datos requeridos para BusinessException
- `codigo`
- `mensaje`
- `resource` (ej: `applicationName + "/" + operation`)
- `applicationName`
- `backend`

## Regla de configuración de errores
- Origen de verdad de códigos/mensajes configurables: Helm (`variables.own.config`).
- `application.yml` solo mapea esas variables con `${...}`.
- El código referencia claves de `application.yml`, no valores literales.

## Ubicación y alcance
Aplicable a:
- `com.pichincha.sp.infrastructure.input.adapter.rest.impl`
- `com.pichincha.sp.infrastructure.input.adapter.rest.validator`
- `com.pichincha.sp.infrastructure.input.adapter.rest.presenter`

```
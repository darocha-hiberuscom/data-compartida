```markdown
# PROMPT BASE — CONTROLLER INPUT ADAPTER REST/SOAP (WEBFLUX)

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la implementación/completado de controllers en `infrastructure.input.adapter.rest.impl` para invocar `PortInput` mediante mappers, con flujo reactivo WebFlux y respetando el contrato vigente (SOAP/XML o REST).

## Política de mapeo (MapStruct)
Aplicar de forma obligatoria la política transversal definida en:
- `.github/prompts/shared/PROMPT_BASE_POLITICA_MAPPERS_MAPSTRUCT.md`

## Reglas obligatorias
- El controller debe depender solo de interfaces `*PortInput`.
- El controller no contiene lógica de negocio; solo validación de entrada técnica, ruteo y delegación.
- Usar mappers en `infrastructure.input.adapter.rest.mapper` para transformar request SOAP/XML a `*RequestDto` de dominio.
- Mantener contrato reactivo con `Mono`.
- Mantener el controller actual (misma clase, endpoint y contrato); ajustar solo la forma de llamada al puerto de entrada.

Para endpoints de detalle transaccional (`getRetrieveExtendedDetailsByFilter`), el controller debe inyectar y usar:
- `TransactDetailStrategy strategy`
- `TransactDetailRequestMapper requestMapper`
- `TransactDetailResponseMapper responseMapper`

## Ubicación y packages
- Controller:
  - `com.pichincha.sp.infrastructure.input.adapter.rest.impl`
- Mapper request controller -> dominio:
  - `com.pichincha.sp.infrastructure.input.adapter.rest.mapper`

## Patrones obligatorios (basado en ejemplo)
- Endpoint SOAP constante con `@RequestMapping`.
- `@PostMapping` con `consumes/produces` XML (`text/xml` y `application/xml`).
- Método `processOperation(...)` que enruta operación mediante wrapper/presenter.
- Métodos privados por operación (`process<Operacion>`) con flujo:
  - validar header/body
  - mapear request SOAP a `RequestDto`
  - invocar `portInput.execute(...)`
  - envolver respuesta SOAP
  - manejar `BusinessException` vs fault genérico
- Logging de inicio/error por operación.

Nota de aplicabilidad:
- Si el controller actual es SOAP/XML, aplicar el patrón SOAP anterior.
- Si el controller actual es REST/OpenAPI, mantener firma y endpoint REST existentes y aplicar únicamente el patrón de delegación a `PortInput`/strategy/mappers.

### Patrón obligatorio — Retrieve Extended Details by Filter
Cuando aplique ese endpoint, implementar flujo equivalente a:

```java
return Mono.fromCallable(() -> requestMapper.toRequest(accountId, entryIdentification,
    creditDebitIndicator, eventType, transactionDate, optionKey, optionValue))
  .flatMapMany(request -> strategy.getStrategy(FlowEnum.fromValue(option))
    .retrieveExtendedTransactDetail(request))
  .flatMapSequential(entry -> Mono.fromCallable(() ->
    responseMapper.toResponse(entry)).subscribeOn(Schedulers.boundedElastic()))
  .collect(CurrentAccountTransactionDetailExtended::new,
    CurrentAccountTransactionDetailExtended::addExtendedEntriesItem)
  .map(data -> new GetRetrieveExtendedDetailsByFilterResponse().data(data))
  .map(ResponseEntity::ok);
```

Reglas de este patrón:
- Construir request únicamente con `requestMapper.toRequest(...)`.
- Seleccionar flujo con `strategy.getStrategy(FlowEnum.fromValue(option))`.
- Mapear respuesta con `responseMapper` en `boundedElastic`.
- Responder con `ResponseEntity.ok(...)`.
- No borrar ni reemplazar el controller existente para aplicar este patrón.
- Si `strategy`, `requestMapper` o `responseMapper` no existen en el controller actual, no crearlos en este paso.
- En ese caso, ajustar únicamente la lógica interna del método con dependencias existentes y registrar `TODO FALTA DETALLE` donde aplique.

## Manejo de errores
- Errores funcionales (`BusinessException`) se transforman a respuesta de negocio SOAP y se consideran respuesta funcional correcta del contrato.
- Errores no controlados se transforman a fault genérico.
- No propagar errores crudos al cliente SOAP.
- Registrar `BusinessException` con `CustomLogLevelHandler` (nivel `ERROR`) con mensaje y detalle.
- En reactivo, resolver `BusinessException` con `onErrorResume(...)` retornando `Mono.just(respuesta de negocio)`.

## Convenciones
- Nombre de controller: `<Servicio>Controller`
- Nombre de mapper request: `<Servicio>SoapRequestMapper`
- Métodos mapper: `to<Operacion>RequestModel(...)`

## Reglas MapStruct
- No realizar mapping manual en controller cuando exista mapper.
- Definir `@Mapping` explícito para campos con nombres distintos.

## Restricción mínima de implementación
- Mantener firma pública, endpoint y contrato de request/response sin cambios.
- Modificar solo la lógica interna del método objetivo.
- No crear clases adicionales (presenters, validators, wrappers o controllers) cuando no existan previamente en el controller.
- Si se requiere mapper adicional, crearlo únicamente en `com.pichincha.sp.infrastructure.input.adapter.rest.mapper` como mapper estructurado (MapStruct).
- No declarar mappers inline dentro del controller (sin clases internas para mapping).

## Alcance
Aplicable a controllers input en:
- `com.pichincha.sp.infrastructure.input.adapter.rest.impl`

```
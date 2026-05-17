````markdown
# IMPLEMENTACIÓN 10 — AJUSTAR CONTROLLER EXISTENTE (CAMBIO INTERNO MÍNIMO)

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs de dominio `record`).
- Paso 02 completado (puertos de entrada `PortInput`).
- Paso 09 completado (services de orquestación implementando `PortInput`).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Existe controller base en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/impl`

## OBJETIVO
Aplicar el ajuste más simple posible sobre el controller existente para delegar al caso de uso (`PortInput`/`strategy`) sin cambiar el contrato.

**Restricción obligatoria:** conservar exactamente la estructura de contrato (firma pública del método, endpoint, request/response, anotaciones públicas) y modificar solo la lógica interna del método.

## ALCANCE
- Completar controllers existentes en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/impl`
- Ajustar únicamente el flujo interno para invocar `PortInput`/`strategy`.
- Crear/ajustar mappers de request/response cuando sea necesario en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/mapper`
- Reusar dependencias ya existentes en el controller y complementar con mappers estructurados solo si el flujo lo requiere.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 2 — Operaciones WSDL del orquestador.
2. Sección 4.8 — Mapeo de entrada del orquestador.
3. Sección 4.9 — Mapeo de salida del orquestador.
4. Sección 4.10 — Errores funcionales.
5. Sección 13 — DTOs request/response por servicio.

Si hay inconsistencias:
- preferir evidencia trazable,
- registrar `FALTA DETALLE` con TODO en el método del controller.

### 2) Completar controller existente
Sobre el controller ya creado en `rest/impl`, completar:
- Ajuste interno del método existente para delegar al puerto/strategy.
- Mantener el método público actual (nombre, parámetros, tipo de retorno) sin cambios.
- Mantener el contrato de entrada/salida y serialización tal como está definido en el proyecto.

Nota de compatibilidad de contrato:
- Si el controller actual es SOAP/XML, mantener `processOperation(...)` y wrappers SOAP existentes.
- Si el controller actual es REST/OpenAPI, mantener firma/endpoint REST existente y aplicar el patrón de invocación a `PortInput`/`strategy` sin migrarlo a SOAP.

Regla crítica de alcance:
- Mantener el controller actual (endpoint, clase, contrato y estructura general).
- No migrar el controller a otro estilo ni reemplazarlo por otro controller.
- Solo ajustar la forma de invocar el puerto/estrategia en el método objetivo.
- No introducir clases auxiliares fuera de la capa `rest/mapper` para cumplir este paso.

Si el endpoint objetivo corresponde a `getRetrieveExtendedDetailsByFilter`, usar este patrón obligatorio:

```java
private final TransactDetailStrategy strategy;
private final TransactDetailRequestMapper requestMapper;
private final TransactDetailResponseMapper responseMapper;

@Override
public Mono<ResponseEntity<GetRetrieveExtendedDetailsByFilterResponse>> getRetrieveExtendedDetailsByFilter(
  String xApp, String xGuid, String xChannel, String xMedium, String xSession, String xDevice,
  String xDeviceIp, String accountId, String entryIdentification, String creditDebitIndicator,
  String eventType, String option, String xAgency, String xGeolocation, Integer offset, Integer limit,
  OffsetDateTime transactionDate, String optionKey, String optionValue, ServerWebExchange exchange) {
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
}
```

Reglas obligatorias para ese patrón:
- No reemplazar `strategy` por llamadas directas a servicios concretos en el controller.
- Usar `requestMapper.toRequest(...)` para construir el request de dominio.
- Usar `responseMapper.toResponse(...)` dentro de `flatMapSequential` con `Schedulers.boundedElastic()`.
- Construir respuesta final con `CurrentAccountTransactionDetailExtended` y `GetRetrieveExtendedDetailsByFilterResponse`.
- No alterar firma pública existente del controller si ya está definida por contrato del proyecto.
- Si `strategy`, `requestMapper` o `responseMapper` no existen actualmente, crearlos como clases separadas en sus packages de mapper/strategy según estándar del proyecto (no inline en controller).

Patrón obligatorio por operación:
1. Log de inicio.
2. Validación de header/body SOAP.
3. Mapeo request SOAP -> `*RequestDto` con mapper.
4. Invocación `portInput.execute(...)`.
5. Construcción/wrap de respuesta SOAP de éxito.
6. Manejo de error con distinción `BusinessException` vs fault genérico.
7. Si ocurre `BusinessException`, loguearla con `CustomLogLevelHandler` (nivel `ERROR`) y responder `Mono.just(respuesta funcional de negocio)`.

### 3) Patrón interno mínimo (con mappers estructurados)
Aplicar un patrón interno simple dentro del método existente:
- construir request de dominio mediante mapper estructurado,
- invocar `PortInput`/`strategy`,
- mapear respuesta al contrato actual mediante mapper estructurado,
- retornar `Mono` respetando la firma vigente.

Reglas obligatorias:
- Usar MapStruct para mappers request/response en este paso.
- Si ya existen `requestMapper/responseMapper/strategy` en el controller, reutilizarlos exactamente como en el ejemplo.
- Si faltan mappers, crearlos como archivos mapper estructurados en la sección/package de mappers:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/mapper`
- No se permite declarar mapper inline dentro de la clase controller (sin clases internas, sin lógica de mapeo extensa embebida).
- Mantener política MapStruct del proyecto para esos mappers.

### 4) Integración con componentes existentes
Si el controller ya tiene componentes de soporte inyectados (strategy, mapper, presenter, validator), reutilizarlos sin crear nuevos.

Reglas:
- No reemplazar componentes existentes sin evidencia.
- No romper el contrato del endpoint SOAP actual.

### 5) Estándar técnico compartido
Aplicar exactamente las reglas del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_INPUT_ADAPTER_CONTROLLER_WEBFLUX.md`

## SALIDA OBLIGATORIA
- Controller existente ajustado internamente para invocar `PortInput`/`strategy`.
- Firma pública, endpoint y contrato de request/response preservados.
- Mappers requeridos creados/ajustados en `.../rest/mapper` usando MapStruct.

## CHECKLIST DE VALIDACIÓN
- [ ] Controller existente en `rest/impl` completado sin cambiar endpoint funcional
- [ ] Firma del método y contrato público se mantienen intactos
- [ ] Cada operación SOAP/REST enruta a su `PortInput`/`strategy`
- [ ] Mappers request/response requeridos creados en `.../rest/mapper`
- [ ] Se usa MapStruct en los mappers creados/ajustados
- [ ] No hay lógica de negocio en controller
- [ ] Manejo de `BusinessException` y fault genérico implementado
- [ ] `BusinessException` se loguea y se responde como salida funcional correcta (`Mono.just(...)`)
- [ ] Contrato reactivo `Mono<...>` preservado
- [ ] En `getRetrieveExtendedDetailsByFilter` se usa el patrón `strategy + requestMapper + responseMapper`
- [ ] Compilación sin errores de imports en controller y mappers

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar operaciones o campos fuera de lo documentado en levantamiento.
- NO borrar ni reemplazar el controller actual; solo ajustar llamada al puerto de entrada con el patrón definido.
- NO modificar la firma pública del método ni la estructura del contrato de entrada/salida.
- Si se requiere mapper adicional, crearlo obligatoriamente en `.../rest/mapper` como mapper estructurado (MapStruct) y nunca dentro del controller.
- Prohibido mapeo inline extenso en el controller cuando el mapeo pueda resolverse con mapper.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_POLITICA_MAPPERS_MAPSTRUCT.md`
  - `.github/prompts/shared/PROMPT_BASE_INPUT_ADAPTER_CONTROLLER_WEBFLUX.md`

````
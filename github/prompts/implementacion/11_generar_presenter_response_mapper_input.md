````markdown
# IMPLEMENTACIÓN 11 — GENERAR PRESENTER/RESPONSE MAPPER INPUT (DOMINIO -> SOAP) CON MAPSTRUCT

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs de dominio `record`).
- Paso 09 completado (services de orquestación implementando `PortInput`).
- Paso 10 completado (controllers input completados y request mapper hacia `PortInput`).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 4.9, 4.10 y 13 disponibles para estructura de respuesta y errores.

## OBJETIVO
Generar/ajustar presenter y mapper de respuesta del input adapter para convertir respuestas de dominio (`*ResponseDto`) a respuestas SOAP/XML, usando MapStruct de forma obligatoria.

## ALCANCE
- Crear/actualizar mappers de respuesta en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/mapper`
- Crear/ajustar presenter/wrapper en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/presenter`
- Integrar mapper de respuesta en controller de `rest/impl`.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.9 — Mapeo de salida del orquestador.
2. Sección 4.10 — Manejo y estructura de errores funcionales.
3. Sección 13 — DTOs response por servicio.
4. Sección 2 — Contratos WSDL/XSD para estructura SOAP final.

Si hay inconsistencias:
- preferir evidencia trazable,
- registrar `FALTA DETALLE` con TODO en mapper/presenter.

### 2) Generar mapper de respuesta (obligatorio MapStruct)
Crear/actualizar mapper dominio -> SOAP response por operación.

Reglas obligatorias:
- No realizar mapping manual en controller cuando exista mapper.
- Un método de mapeo por respuesta de operación.
- Cumplir de forma obligatoria la política de mapeo MapStruct definida en el prompt compartido.

### 3) Ajustar presenter/wrapper
En presenter/wrapper:
- Reusar mapper MapStruct para construir payload SOAP de éxito/error.
- Mantener patrón existente de `wrap...Response(...)` y `build...Success/Error(...)` cuando aplique.

Reglas:
- No incluir lógica de negocio.
- Mantener compatibilidad con fault genérico y `BusinessException`.
- Cuando exista `BusinessException`, construir respuesta de negocio vía presenter como salida funcional correcta del contrato.
- Registrar `BusinessException` con `CustomLogLevelHandler` (nivel `ERROR`) y resolver en reactivo con `Mono.just(respuesta de negocio)`.

### 4) Integración con controller
Actualizar controller de input para usar presenter + response mapper MapStruct.

Reglas:
- No modificar endpoint ni contrato SOAP existente.
- Preservar contrato reactivo `Mono<SoapEnvelopeResponse>`.

### 5) Estándar técnico compartido
Aplicar exactamente las reglas de:
- `.github/prompts/shared/PROMPT_BASE_INPUT_ADAPTER_CONTROLLER_WEBFLUX.md`
- `.github/prompts/shared/PROMPT_BASE_INPUT_RESPONSE_MAPPER_MAPSTRUCT.md`

## SALIDA OBLIGATORIA
- Mapper(s) MapStruct dominio -> SOAP response creados/actualizados.
- Presenter/wrapper integrado con mapper(s) MapStruct.
- Controller usando presenter/mapper para respuesta final por operación.

## CHECKLIST DE VALIDACIÓN
- [ ] Se cumple la política de mapeo MapStruct del prompt compartido
- [ ] No hay mapeo manual de response en controller cuando existe mapper
- [ ] Presenter/wrapper usa mapper para construir respuestas SOAP
- [ ] Manejo de éxito/error alineado con secciones 4.9 y 4.10
- [ ] `BusinessException` se transforma en respuesta funcional correcta y se loguea
- [ ] Endpoint SOAP y contrato de salida preservados
- [ ] Compilación sin errores de imports/anotaciones MapStruct

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar campos/estructuras SOAP fuera del levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_POLITICA_MAPPERS_MAPSTRUCT.md`
  - `.github/prompts/shared/PROMPT_BASE_INPUT_RESPONSE_MAPPER_MAPSTRUCT.md`

````
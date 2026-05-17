````markdown
# IMPLEMENTACIÓN 13 — ACOPLAR USO DE EXCEPCIONES AL MODELO DE INPUT ADAPTER

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 10 completado (controller input completado con `PortInput`).
- Paso 11 completado (presenter/response mapper input completados).
- Paso 12 completado (validaciones funcionales del legado implementadas).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Sección 4.10 disponible (códigos/mensajes de error funcional).

## OBJETIVO
Acoplar el manejo de excepciones de la solución migrada al modelo legado de `input exception`, siguiendo el patrón del controller de referencia: `BusinessException` para errores funcionales y fault genérico para errores no controlados.

## ALCANCE
- Ajustar manejo de excepciones en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/impl`
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/validator`
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/presenter`
- Preservar contratos SOAP/XML y patrón reactivo `Mono`.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.10 — Consolidado de errores (códigos/mensajes funcionales).
2. Sección 4.8 — Validaciones de entrada del orquestador.
3. Sección 4.9 — Estructura de salida y propagación de errores.
4. Sección 4.7 — Flujo de invocaciones y condiciones de error por operación.

Si hay inconsistencias:
- preferir evidencia trazable,
- registrar `FALTA DETALLE` con TODO en el punto de manejo de excepción.

### 2) Acoplar patrón de excepciones en controller
Implementar/ajustar por operación el patrón:
- `validateSoapRequest(...)` para validar header/body.
- `BusinessException` para errores funcionales de validación/orquestación.
- `isBusinessException(...)` para discriminar en `onErrorResume`.
- `onErrorResume(...)`:
  - si `BusinessException` -> respuesta funcional por operación (`build...Error` + `wrap...Response`).
  - otro caso -> `wrapGenericFault(buildGenericFault())`.
- `BusinessException` debe loguearse con `CustomLogLevelHandler` (nivel `ERROR`) con mensaje y detalle.
- La rama `BusinessException` en `onErrorResume(...)` debe responder como salida funcional correcta (`Mono.just(...)`) y no como fallo técnico.

Pipeline reactivo recomendado por operación (input):
- `Mono.justOrEmpty(request)`
  `.flatMap(req -> validateSoapRequest(...).thenReturn(req))`
  `.map(requestMapper::to...RequestModel)`
  `.flatMap(portInput::execute)`
  `.map(responseWrapper::wrap...Success)`
  `.doOnError(...)`
  `.onErrorResume(error -> isBusinessException(error) ? buildFunctionalError(...) : buildGenericFault(...))`

### 3) Reglas de construcción de BusinessException
Al crear `BusinessException`, usar datos del modelo legado:
- código y mensaje funcional alineados con sección 4.10,
- `resource` por operación (ej: `applicationName/Operacion`),
- `applicationName` y `backend` como metadatos.

Reglas obligatorias de configuración:
- Códigos y mensajes de error configurables deben leerse desde `application.yml` (`@Value`/`@ConfigurationProperties`).
- `application.yml` debe mapear esos valores desde variables de entorno `${...}`.
- Esas variables deben existir en Helm configmap por ambiente.
- Evitar hardcode de códigos/mensajes en controller/service salvo constantes técnicas no configurables y justificadas.

### 4) Integración con validadores y presenter
- Reusar validadores de input adapter para reglas de header/body.
- Reusar presenter/wrapper para construir respuestas de error funcional y fault genérico.
- No duplicar lógica de construcción de errores en múltiples capas.

### 5) Estándar técnico compartido
Aplicar exactamente las reglas de:
- `.github/prompts/shared/PROMPT_BASE_MODELO_EXCEPCIONES_INPUT.md`

## SALIDA OBLIGATORIA
- Controllers input ajustados al modelo de excepciones legado.
- Validaciones que disparan `BusinessException` con datos completos.
- Manejo reactivo uniforme por operación (`BusinessException` vs fault genérico).

## CHECKLIST DE VALIDACIÓN
- [ ] Se usa `BusinessException` para errores funcionales evidenciados
- [ ] Se diferencia correctamente error funcional vs técnico no controlado
- [ ] `onErrorResume` por operación aplica patrón legado
- [ ] Pipeline reactivo de input (`validateSoapRequest` + `onErrorResume`) aplicado de forma consistente
- [ ] Fault genérico se usa solo para errores no controlados
- [ ] `BusinessException` se loguea y se responde como salida funcional correcta (`Mono.just(...)`)
- [ ] Códigos/mensajes alineados con sección 4.10
- [ ] Códigos/mensajes configurables leídos desde `application.yml` (sin hardcode)
- [ ] Variables de error requeridas existen en Helm por ambiente
- [ ] `resource`, `applicationName` y `backend` presentes cuando aplique
- [ ] Compilación sin errores de imports en controller/validator/presenter

## REGLAS
- NO inventar códigos/mensajes de error fuera del levantamiento.
- NO romper contrato SOAP/XML existente.
- NO modificar lógica de negocio fuera del alcance de manejo de excepciones.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_MODELO_EXCEPCIONES_INPUT.md`

````
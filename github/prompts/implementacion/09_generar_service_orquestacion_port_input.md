````markdown
# IMPLEMENTACIÓN 09 — GENERACIÓN DE SERVICE DE ORQUESTACIÓN IMPLEMENTANDO PORT INPUT

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs de dominio como `record`).
- Paso 02 completado (puertos de entrada `PortInput`).
- Paso 03 completado (puertos de salida `PortOutput`).
- Paso 06 completado (adapters output implementados).
- Paso 08 completado (manejo técnico de errores en adapters).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 4.7, 4.8, 4.9, 4.10, 12 y 13 disponibles.

## OBJETIVO
Generar services de aplicación que implementen los puertos de entrada (`PortInput`) y ejecuten la lógica de orquestación del orquestador basándose en el levantamiento usando patrón **Strategy** por `flujo` y `estado`.

## ALCANCE
- Crear services en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/service`
- Generar cobertura 1:1: un service por cada `PortInput`.
- Implementar flujo de orquestación invocando `PortOutput` según el orden y reglas del levantamiento.
- Mantener contrato reactivo `Mono<...>`.
- Crear estrategias por flujo/estado en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/service/strategy`

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.8 — Mapeo de entrada del orquestador.
2. Sección 4.9 — Mapeo de salida del orquestador.
3. Sección 4.7 — Mapeo de APIs/servicios invocados y secuencia de invocaciones.
4. Sección 4.10 — Consolidado de errores.
5. Sección 12 — Resumen global de servicios invocados.
6. Sección 13 — DTOs de entrada/salida por servicio.

Si hay inconsistencias:
- preferir evidencia trazable,
- marcar `FALTA DETALLE` con TODO en el service cuando falten reglas funcionales.

### 2) Implementación por puerto de entrada
Por cada `PortInput` del paso 02, crear una implementación `Service` que:
- implemente la interfaz `PortInput`,
- reciba `RequestDto` del orquestador,
- delegue la orquestación a una estrategia seleccionada por `flujo/estado`,
- construya `ResponseDto` final del orquestador.

Reglas obligatorias:
- Debe existir correspondencia 1:1 entre `PortInput` y `Service`.
- El service no debe depender de adapters concretos, solo de interfaces `PortOutput`.
- El `Service` no debe contener múltiples `if/switch` de negocio por `flujo/estado` cuando esa lógica pueda delegarse a estrategias.
- No usar SLF4J en services (`@Slf4j`, `LoggerFactory`, `log.*`).
- Anotar métodos de service con `@BpLogger`.
- Inyectar `private final CustomLogLevelHandler customLogLevelHandler;`.

### 2.1) Patrón Strategy obligatorio (flujo/estado)
Implementar separación de lógica por `flujo` y `estado`:
- `OrquestacionStrategy` (interfaz base)
- `OrquestacionStrategyResolver` (selecciona estrategia por `flujo/estado`)
- Una clase concreta por variante funcional (ejemplo: `IniciarActivaStrategy`, `ConsultarActivaStrategy`, `IniciarInactivaStrategy`)

Contrato sugerido:
- `boolean supports(String flujo, String estado)`
- `Mono<ResponseDto> execute(RequestDto request)`

Reglas:
- El service principal implementa `PortInput` y únicamente:
  - extrae `flujo/estado` del request,
  - selecciona estrategia vía resolver,
  - delega ejecución a `strategy.execute(...)`.
- Cada estrategia encapsula su secuencia propia de invocaciones `PortOutput`.
- Evitar lógica sobrespuesta en una única clase service.

### 3) Lógica de orquestación
Implementar la secuencia funcional de acuerdo con levantamiento:
- orden de invocaciones de servicios,
- mapeos request/response entre pasos,
- decisiones y bifurcaciones evidenciadas,
- consolidación de respuesta final.

Reglas:
- No inventar reglas de negocio no documentadas.
- Si una validación/regla no está evidenciada, registrar `FALTA DETALLE`.
- Las decisiones por `flujo/estado` deben quedar en estrategias separadas, no en un bloque monolítico.
- En flujos reactivos, usar `doOnError(...)` con este patrón:

```java
.doOnError(error -> customLogLevelHandler.log(
  CustomLogLevel.ERROR,
  Thread.currentThread().getStackTrace(),
  error.getMessage() == null ? DomainValidationConstants.OPERATION_FLOW_ERROR : error.getMessage(),
  error.getMessage()))
```

### 4) Manejo de errores funcionales
Implementar manejo de errores funcionales solo cuando esté evidenciado en la sección 4.10.

Reglas:
- Diferenciar errores técnicos (infraestructura) de errores funcionales (orquestación).
- Preservar trazabilidad de código/mensaje funcional en la respuesta del orquestador cuando aplique.
- Cuando exista un error funcional evidenciado, generar una excepción de negocio (según modelo vigente del proyecto) con código y mensaje funcional.
- La excepción de negocio debe registrarse explícitamente con `CustomLogLevelHandler` (nivel `ERROR`) incluyendo mensaje y detalle.
- La excepción de negocio NO debe terminar el flujo como error técnico: debe tratarse como respuesta funcional válida del orquestador.
- En reactivo, resolver con `onErrorResume(...)` para excepciones de negocio y retornar `Mono.just(ResponseDto con error funcional)`.
- Solo errores técnicos no funcionales deben propagarse como error reactivo.

### 5) Estándar técnico compartido
Aplicar exactamente las reglas del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_APPLICATION_SERVICE_ORQUESTACION.md`

## SALIDA OBLIGATORIA
- Services de orquestación creados en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/service`
- Estrategias de orquestación creadas en `.../application/service/strategy`.
- Cada service implementa su `PortInput`, selecciona estrategia por `flujo/estado` y delega la orquestación `PortOutput`.

## CHECKLIST DE VALIDACIÓN
- [ ] Se generó un `Service` por cada `PortInput`
- [ ] Todos los services implementan su interfaz `PortInput`
- [ ] Orquestación implementada con `PortOutput` (sin dependencias a adapters)
- [ ] Existe patrón Strategy por `flujo/estado` (interfaz + resolver + estrategias concretas)
- [ ] El service principal delega en estrategia y evita lógica monolítica por `flujo/estado`
- [ ] Sin uso de SLF4J en services
- [ ] Métodos de service anotados con `@BpLogger`
- [ ] Uso de `CustomLogLevelHandler` en `doOnError(...)`
- [ ] Flujo funcional alineado con secciones 4.7, 4.8, 4.9, 12 y 13
- [ ] Manejo de errores funcionales alineado con sección 4.10
- [ ] Errores funcionales generan excepción de negocio y se loguean
- [ ] Excepción de negocio se transforma a respuesta funcional válida (`Mono.just(ResponseDto)`)
- [ ] Contrato reactivo `Mono<...>` preservado
- [ ] Compilación sin errores de imports en services generados

## REGLAS
- NO modificar lógica del negocio existente fuera del alcance del service.
- NO inventar reglas funcionales o validaciones no documentadas.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_APPLICATION_SERVICE_ORQUESTACION.md`

````
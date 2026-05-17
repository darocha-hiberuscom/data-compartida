```markdown
# PROMPT BASE — SERVICE DE APLICACIÓN PARA ORQUESTACIÓN

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la implementación de services de aplicación que orquestan casos de uso e implementan puertos de entrada (`PortInput`) usando patrón **Strategy** por `flujo/estado` cuando existan variantes funcionales.

## Reglas obligatorias
- El service debe implementar una interfaz `*PortInput` de `application.port.input`.
- La orquestación debe invocar puertos de salida (`*PortOutput`) y no adapters concretos.
- No incluir lógica de infraestructura dentro del service.
- Mantener flujo reactivo con `Mono`.
- Separar lógica por `flujo/estado` usando estrategias (evitar service monolítico con múltiples ramas de negocio).
- No usar logs con SLF4J (`@Slf4j`, `LoggerFactory`, `log.*`) en services.
- Anotar métodos de service con `@BpLogger`.
- Inyectar `private final CustomLogLevelHandler customLogLevelHandler;`.

## Ubicación y package
- Package base obligatorio:
  - `com.pichincha.sp.application.service`

## Convenciones de nombre
- Clase service: `<Operacion>Service`
- Implementa: `<Operacion>PortInput`
- Método principal: `execute(...)`

## Patrón Strategy (obligatorio cuando aplique)
- Interfaz base: `<Operacion>Strategy` o `OrquestacionStrategy`
- Selector/resolver: `<Operacion>StrategyResolver`
- Implementaciones concretas: una por combinación de `flujo/estado`

Contrato sugerido:
- `boolean supports(String flujo, String estado)`
- `Mono<ResponseDto> execute(RequestDto request)`

Reglas:
- El service principal solo selecciona estrategia y delega.
- Cada estrategia encapsula su secuencia de invocaciones `PortOutput`.
- Reglas funcionales por `flujo/estado` no deben quedar sobrepuestas en una sola clase.

## Reglas de orquestación
- Mapear `RequestDto` de entrada a invocaciones necesarias de salida.
- Coordinar llamadas a uno o varios `PortOutput` según el flujo documentado.
- Consolidar resultado en `ResponseDto` de salida.
- Respetar el orden de invocación y reglas de decisión documentadas en levantamiento.
- Si hay lógica distinta por estado/flujo, resolver vía Strategy y no con cascadas extensas de `if/switch` en el service principal.

## Reglas de errores
- No propagar excepciones técnicas crudas sin contexto.
- Delegar errores técnicos de integración al manejo definido en infraestructura.
- Aplicar mapeo/control de error funcional solo cuando esté evidenciado en levantamiento.
- Cuando exista error funcional evidenciado, generar excepción de negocio (modelo vigente) con código y mensaje funcional.
- Loguear explícitamente la excepción de negocio con `CustomLogLevelHandler` en nivel `ERROR`.
- Tratar la excepción de negocio como respuesta funcional válida del orquestador y no como error técnico.
- En reactivo, manejar excepción de negocio con `onErrorResume(...)` y responder con `Mono.just(ResponseDto con error funcional)`.
- Solo errores técnicos no funcionales deben propagarse como error reactivo.
- En flujos reactivos del service, usar `doOnError(...)` con el patrón:

```java
.doOnError(error -> customLogLevelHandler.log(
  CustomLogLevel.ERROR,
  Thread.currentThread().getStackTrace(),
  error.getMessage() == null ? DomainValidationConstants.OPERATION_FLOW_ERROR : error.getMessage(),
  error.getMessage()))
```

## Alcance
Aplicable a services en:
- `com.pichincha.sp.application.service`

```
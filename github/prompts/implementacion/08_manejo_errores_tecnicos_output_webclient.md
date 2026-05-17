````markdown
# IMPLEMENTACIÓN 08 — MANEJO DE ERRORES TÉCNICOS EN ADAPTERS OUTPUT (WEBCLIENT)

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 06 completado (adapters output implementados con `WebClient`).
- Paso 07 completado (configuración de `WebClient` y timeouts externalizados).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Sección 4.7 disponible para trazabilidad de invocaciones.

## OBJETIVO
Implementar un manejo estándar de errores técnicos para todas las invocaciones externas realizadas por adapters output con `WebClient`, con trazabilidad completa y mapeo a excepciones de infraestructura.

El manejo debe aplicarse sobre el bean `WebClient` específico de cada adapter (ej: `customerPaymentsWebClient`) alineado con el paso 07.

Adicionalmente, cuando el error se propague al servicio SOAP, la estructura de salida de error debe mantenerse únicamente como SOAP Fault `500`, resuelta por `ErrorResolverHandler` + `ErrorResolver`.

## ALCANCE
- Actualizar adapters en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter`
- Crear/actualizar excepciones técnicas en infraestructura (si no existen).
- Asegurar comportamiento homogéneo ante `4xx`, `5xx`, timeout y conectividad.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.7 — Servicios invocados, endpoints y contexto técnico de integración.
2. Sección 12 — Resumen global de servicios invocados.
3. Sección 13 — Contratos request/response por servicio.

Si falta información:
- no inventar,
- registrar `FALTA DETALLE` con pista trazable.

### 2) Cobertura de errores por adapter
Para cada adapter output del paso 06, implementar manejo explícito de:
- HTTP `4xx`
- HTTP `5xx`
- timeout
- conectividad
- parseo/deserialización

Patrón reactivo obligatorio por operación:
- `executeRequest(...)`
  `.doOnNext(...)`
  `.onErrorMap(this::mapError)`
  `.flatMap(this::validateResponse)`

Reglas obligatorias:
- Preservar retorno `Mono<...>` y flujo reactivo.
- No mezclar validaciones de negocio con errores técnicos de integración.
- No usar `WebClient.Builder` genérico en adapters; usar `WebClient` inyectado por cliente.
- Si existen múltiples beans `WebClient`, usar `@Qualifier("<cliente>WebClient")`.
- En todos los métodos principales de adapters, usar `@EventAudit(service = "...", method = "...", type = AuditType.T)`.
- No usar SLF4J (`@Slf4j`, `LoggerFactory`, `log.*`).
- Usar `@BpLogger` en métodos del adapter.
- Inyectar `private final CustomLogLevelHandler customLogLevelHandler;`.
- Usar `doOnError(...)` con este patrón:

```java
.doOnError(error -> customLogLevelHandler.log(
  CustomLogLevel.ERROR,
  Thread.currentThread().getStackTrace(),
  error.getMessage() == null ? DomainValidationConstants.OPERATION_FLOW_ERROR : error.getMessage(),
  error.getMessage()))
```

### 3) Mapeo a excepciones de infraestructura
Aplicar mapeo de errores técnicos a excepciones del proyecto.

Reglas:
- No propagar `WebClientResponseException` ni excepciones técnicas crudas directamente.
- Incluir contexto técnico mínimo (servicio, operación, endpoint, estado HTTP, causa original).
- Mantener nombres y paquetes consistentes con convenciones del proyecto.

### 4) Trazabilidad y observabilidad
Registrar información suficiente para diagnóstico técnico:
- servicio invocado,
- operación/adapter,
- endpoint,
- estado HTTP (si aplica),
- mensaje técnico original.

Reglas:
- Evitar logs con datos sensibles.
- Priorizar mensajes accionables para soporte técnico.

### 5) Estándar técnico compartido
Aplicar exactamente las reglas del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_OUTPUT_ADAPTER_WEBCLIENT_ERRORES.md`
- `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER.md`
- `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER_MIN.md`

### 6) Política obligatoria de errores SOAP (solo 500)
Cuando el flujo técnico de adapters llegue a la capa SOAP, aplicar estas reglas:

- Estructura de error SOAP única en estado `500` (`SoapFaultEnvelopeDto` / `SoapFaultBodyDto` / `SoapFaultDto`).
- Captura y resolución centralizada mediante:
  - `infrastructure.exception.error.resolver.handler.ErrorResolverHandler`
  - `infrastructure.exception.error.resolver.ErrorResolver`
  - `GlobalErrorExceptionResolver`
  - `ResponseStatusExceptionResolver`
  - `UnexpectedErrorResolver`

Errores que deben considerarse manejables en el servicio SOAP:
- `GlobalErrorException`
- `ResponseStatusException`
- `ConstraintViolationException` (fallback del handler)
- `Throwable` no controlado (fallback a `UnexpectedErrorResolver`)

Reglas:
- No definir estructuras SOAP alternativas para 4xx en la salida final del servicio SOAP.
- No duplicar manejo SOAP de errores en adapters output; el punto central es `ErrorResolverHandler`.
- Si falta detalle funcional/técnico, registrar `FALTA DETALLE` con trazabilidad, manteniendo formato SOAP 500.

## SALIDA OBLIGATORIA
- Adapters output con manejo homogéneo de errores técnicos.
- Excepciones de infraestructura definidas/reusadas para integración externa.
- Trazabilidad técnica suficiente en fallos de consumo externo.

## CHECKLIST DE VALIDACIÓN
- [ ] Todos los adapters output cubren `4xx` y `5xx`
- [ ] Todos los adapters output cubren timeout y conectividad
- [ ] Errores técnicos se mapean a excepciones de infraestructura
- [ ] No se propagan excepciones técnicas crudas
- [ ] Cada adapter aplica manejo de errores sobre su bean `WebClient` específico
- [ ] Todos los métodos principales de adapters tienen `@EventAudit(service=..., method=..., type=AuditType.T)`
- [ ] Sin uso de SLF4J en adapters
- [ ] Métodos de adapters anotados con `@BpLogger`
- [ ] Uso de `CustomLogLevelHandler` en `doOnError(...)`
- [ ] Pipeline reactivo `executeRequest`/`onErrorMap`/`validateResponse` aplicado de forma consistente
- [ ] Contrato reactivo `Mono<...>` preservado
- [ ] Trazabilidad técnica mínima incluida en errores
- [ ] Estructura de error SOAP final unificada en `500`
- [ ] Captura de errores SOAP centralizada con `ErrorResolverHandler`
- [ ] Tipos de error SOAP manejables alineados a los resolvers existentes
- [ ] Compilación sin errores de imports/excepciones

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar comportamiento funcional no documentado.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_OUTPUT_ADAPTER_WEBCLIENT_ERRORES.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER_MIN.md`

````
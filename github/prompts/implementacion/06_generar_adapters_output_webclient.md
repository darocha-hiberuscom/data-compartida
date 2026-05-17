````markdown
# IMPLEMENTACIÓN 06 — GENERACIÓN DE ADAPTERS OUTPUT CON WEBCLIENT (WEBFLUX)

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs de dominio generados como `record`).
- Paso 03 completado (puertos de salida `PortOutput` generados).
- Paso 04 completado (models request/response de adapter output generados).
- Paso 05 completado (mappers dominio ↔ adapter output generados con MapStruct).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 4.7, 12 y 13 disponibles para endpoints, mapeos y contratos.

## OBJETIVO
Generar adapters de salida en infraestructura que implementen los `PortOutput` y realicen invocaciones a servicios externos usando beans `WebClient` específicos por cliente (según configuración del paso 07).

## ALCANCE
- Crear implementaciones en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter`
- Generar cobertura 1:1: un adapter por cada puerto de salida (`PortOutput`).
- Reusar los models `request/response` del paso 04.
- Aplicar reglas técnicas definidas en prompt compartido.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.7 — Mapeo de APIs / servicios invocados (invocación, endpoint, mapeos de request/response).
2. Sección 12 — Resumen global de servicios invocados.
3. Sección 13 — DTOs por servicio.
4. Sección 2 — Contratos WSDL/XSD (validación de tipos/estructuras cuando aplique).

Si hay inconsistencias:
- preferir lo evidenciado con trazabilidad,
- y marcar `FALTA DETALLE` en TODO del adapter cuando falte dato técnico.

### 2) Cobertura por puerto de salida
Por cada `PortOutput` generado en el paso 03, crear una clase adapter que:
- implemente el puerto correspondiente,
- reciba un DTO de dominio request,
- mapee a request técnico del adapter output,
- invoque el servicio con `WebClient`,
- mapee el response técnico a DTO de dominio response,
- retorne `Mono<...>`.

Patrón recomendado por operación (según ejemplo legado):
- método del adapter anotado obligatoriamente con:

```java
@EventAudit(service = "WSClientes0001", method = "ConsultarCuentasActivas01", type = AuditType.T)
```

Parametrización por operación real:
- `service` = servicio invocado según levantamiento.
- `method` = operación invocada según levantamiento.
- `type` = `AuditType.T`.

- `return executeRequest(requestDto)`
  `.doOnNext(...)`
  `.onErrorMap(this::mapError)`
  `.flatMap(this::validateResponse);`

Reglas obligatorias:
- Debe existir correspondencia 1:1 entre `PortOutput` y adapter.
- Si falta evidencia de endpoint o mapeo en sección 4.7, no inventar y registrar `FALTA DETALLE` con pista trazable.
- Cada adapter debe inyectar su bean `WebClient` específico (ej: `customerPaymentsWebClient`), no un builder genérico.
- No generar ni usar `CommonHttpHeadersOperator` en los adapters.
- No usar SLF4J en adapters (`@Slf4j`, `LoggerFactory`, `log.*`).
- Anotar métodos del adapter con `@BpLogger`.
- Inyectar `private final CustomLogLevelHandler customLogLevelHandler;` para trazabilidad.

### 3) Convención y estructura técnica
Aplicar exactamente las reglas del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_OUTPUT_ADAPTER_WEBCLIENT.md`

### 4) Dependencias y paquetes
- Implementar en package:
  - `com.pichincha.sp.infrastructure.output.adapter`
- Usar `WebClient` de:
  - `org.springframework.web.reactive.function.client.WebClient`
- Mantener contrato reactivo con:
  - `reactor.core.publisher.Mono`
- Declarar adapters con `@Component` e inyección por constructor (`@RequiredArgsConstructor` o equivalente).
- Si hay múltiples beans `WebClient`, usar `@Qualifier("<cliente>WebClient")`.

### 5) Compatibilidad con modelos y puertos
- Reusar `PortOutput` del paso 03 (no redefinir interfaces).
- Reusar records request/response del paso 04 (no redefinir modelos técnicos).
- Reusar mappers MapStruct del paso 05 para conversiones dominio↔adapter.
- Evitar mapeo inline manual en adapters cuando exista mapper.
- Mantener alineación de campos con DTOs de dominio del paso 01.

## SALIDA OBLIGATORIA
- Clases adapter output creadas en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter`
- Cada clase implementa su `PortOutput` y usa su bean `WebClient` específico para invocar el servicio correspondiente.

## CHECKLIST DE VALIDACIÓN
- [ ] Se generó un adapter por cada `PortOutput`
- [ ] Todos los adapters implementan interfaces `PortOutput`
- [ ] Todos los adapters usan `WebClient` (WebFlux)
- [ ] Cada adapter usa bean `WebClient` específico por cliente (sin `WebClient.Builder` genérico)
- [ ] Sin uso de SLF4J en adapters
- [ ] Métodos de adapters anotados con `@BpLogger`
- [ ] Adapters inyectan `CustomLogLevelHandler`
- [ ] Todos los métodos principales de adapters tienen `@EventAudit(service=..., method=..., type=AuditType.T)`
- [ ] Flujo request(domain) -> request(adapter) -> response(adapter) -> response(domain) implementado
- [ ] Conversiones dominio↔adapter realizadas mediante mappers MapStruct reutilizables
- [ ] Pipeline reactivo con `onErrorMap` y `validateResponse` aplicado por operación
- [ ] Contrato reactivo `Mono<...>` preservado
- [ ] No hay lógica de negocio en adapters
- [ ] Endpoints y mapeos basados en levantamiento (4.7, 12, 13)
- [ ] Errores técnicos manejados con trazabilidad
- [ ] Compilación sin errores de imports en adapters generados

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar endpoints, headers o mapeos fuera de lo evidenciado.
- NO crear clase `CommonHttpHeadersOperator`.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_OUTPUT_ADAPTER_WEBCLIENT.md`

````
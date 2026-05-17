```markdown
# PROMPT BASE — VALIDACIONES FUNCIONALES DEL LEGADO

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la implementación de validaciones funcionales heredadas del proyecto legado, usando evidencia del levantamiento y sin inventar reglas.

## Reglas obligatorias
- Implementar solo validaciones evidenciadas en `LEVANTAMIENTO_INFORMACION.md`.
- Mantener trazabilidad entre validación, código/mensaje de error y origen documental.
- No mezclar errores técnicos de infraestructura con errores funcionales de negocio.
- No alterar contratos públicos (`PortInput`, controller SOAP, DTOs) salvo que el levantamiento lo exija.

## Fuentes prioritarias
1. Sección 4.10 — Consolidado de mensajes de error.
2. Sección 4.8 — Mapeo de entrada del orquestador.
3. Sección 4.9 — Mapeo de salida del orquestador.
4. Sección 4.7 — Flujo de invocaciones y reglas de orquestación.

## Reglas de implementación
- Ubicar validaciones en capa de aplicación y/o validator de input adapter según corresponda.
- Propagar códigos/mensajes funcionales conforme al patrón del proyecto.
- No hardcodear códigos/mensajes funcionales configurables; leerlos desde `application.yml` (`@Value`/`@ConfigurationProperties`).
- Garantizar que esas propiedades estén externalizadas con `${...}` y declaradas en Helm (`variables.own.config[].name`) para `dev/test/prod`.
- Para faltantes de evidencia, marcar `FALTA DETALLE` y `TODO` trazable.
- Implementar validaciones con diseño mantenible: catálogo/lista de reglas, strategy/specification o patrón equivalente.
- Evitar cadenas largas de `if` secuenciales por campo en validators/services.
- Centralizar el factory/método de construcción de `BusinessException` para minimizar duplicación.

## Regla de mantenibilidad (obligatoria)
- Si una operación tiene múltiples validaciones funcionales (>= 4), modelarlas como reglas declarativas reutilizables.
- La evaluación de reglas debe permitir extensión por nuevas reglas sin reescribir el flujo principal del validador.
- El flujo debe devolver el primer error funcional aplicable según prioridad/evidencia del levantamiento.

## Alcance
Aplicable a validaciones en:
- `com.pichincha.sp.application.service`
- `com.pichincha.sp.infrastructure.input.adapter.rest.validator`

```
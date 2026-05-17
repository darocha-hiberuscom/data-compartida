```markdown
# PROMPT BASE — POLÍTICA ÚNICA DE MAPPERS CON MAPSTRUCT

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Definir una política transversal y única para cualquier mapper generado en el proyecto.

## Política obligatoria
- El uso de MapStruct es obligatorio en todos los mappers (`@Mapper(componentModel = "spring")`).
- El mapeo debe resolverse principalmente con anotaciones (`@Mapping`, `@BeanMapping`, `@Named`, etc.).
- No se deben generar funciones manuales de mapeo cuando MapStruct lo pueda resolver.
- Solo se permite mapeo manual (por ejemplo `default method`) cuando sea indispensable por transformación compleja no resoluble con anotaciones.
- Cuando se use mapeo manual indispensable, dejar justificación breve y trazable en el mapper.

## Aplicación
Esta política aplica a:
- Mappers de input adapter (request/response SOAP/XML).
- Mappers de output adapter (dominio ↔ request/response técnico).
- Cualquier mapper adicional creado en pasos futuros de implementación.

## Regla de referencia
Todo paso de implementación que genere mappers debe referenciar este archivo como regla obligatoria.

```
```markdown
# PROMPT BASE — MAPPERS DOMINIO ↔ ADAPTER OUTPUT

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la creación de mappers entre DTOs de dominio y modelos técnicos de adapter output (`request/response`).

## Política de mapeo (MapStruct)
Aplicar de forma obligatoria la política transversal definida en:
- `.github/prompts/shared/PROMPT_BASE_POLITICA_MAPPERS_MAPSTRUCT.md`

## Reglas obligatorias
- Mantener separación de capas: dominio no conoce modelos técnicos de infraestructura.
- Mappers sin lógica de negocio; solo transformación de datos.
- Soportar mapeo bidireccional cuando aplique:
  - Dominio request -> Adapter request
  - Adapter response -> Dominio response
- Preservar nulabilidad y estructura de colecciones.

## Ubicación y package
- Package base recomendado:
  - `com.pichincha.sp.infrastructure.output.adapter.mapper`

## Convenciones de nombre
- Mapper por servicio/operación:
  - `<Servicio><Operacion>Mapper`
- Métodos recomendados:
  - `toAdapterRequest(...)`
  - `toDomainResponse(...)`

## Reglas de implementación
- Reusar records existentes (paso 01 y paso 04); no redefinir modelos.
- Si falta un campo en levantamiento, marcar `FALTA DETALLE` / `TODO: verificar tipo`.
- Configurar MapStruct con `componentModel = "spring"`.
- Declarar `@Mapping` explícito cuando existan diferencias de nombres o estructura.
- Usar anotaciones de MapStruct como mecanismo principal de transformación.
- Evitar dependencias de framework innecesarias adicionales a MapStruct.

## Alcance
Aplicable a mappers para:
- `com.pichincha.sp.infrastructure.output.adapter.request`
- `com.pichincha.sp.infrastructure.output.adapter.response`
- `com.pichincha.sp.domain.dto`

```
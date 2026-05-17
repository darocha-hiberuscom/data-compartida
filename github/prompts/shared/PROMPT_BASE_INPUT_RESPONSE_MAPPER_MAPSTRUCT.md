```markdown
# PROMPT BASE — RESPONSE MAPPER INPUT (DOMINIO -> SOAP) CON MAPSTRUCT

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar el mapeo de respuestas desde DTOs de dominio hacia modelos SOAP/XML del input adapter usando MapStruct.

## Política de mapeo (MapStruct)
Aplicar de forma obligatoria la política transversal definida en:
- `.github/prompts/shared/PROMPT_BASE_POLITICA_MAPPERS_MAPSTRUCT.md`

## Reglas obligatorias
- No se permite mapeo manual en controller/presenter cuando exista mapper.
- El mapper transforma `*ResponseDto` de dominio a objetos SOAP de respuesta (`generated.*` o DTO response del adapter).
- Sin lógica de negocio en mapper.

## Ubicación y package
- `com.pichincha.sp.infrastructure.input.adapter.rest.mapper`

## Convenciones
- Nombre de mapper: `<Servicio>SoapResponseMapper`
- Métodos sugeridos:
  - `toSoapResponse(...)`
  - `toSoapBodyResponse(...)`

## Reglas MapStruct
- Configurar `componentModel = "spring"`.
- Declarar `@Mapping` explícito cuando nombres difieran.
- Para transformaciones complejas, usar `default` methods solo si es indispensable y con justificación trazable.

## Alcance
Aplicable a mappers de salida SOAP en:
- `com.pichincha.sp.infrastructure.input.adapter.rest.mapper`

```
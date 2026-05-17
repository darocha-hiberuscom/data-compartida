```markdown
# PROMPT BASE — RECORDS DE ADAPTER OUTPUT (REQUEST/RESPONSE)

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la generación de modelos de transporte `request/response` en la capa `infrastructure.output.adapter` para invocaciones a servicios externos.

## Reglas obligatorias
- Todos los modelos deben implementarse como `record`.
- No usar Lombok en estos modelos (`@Data`, `@Getter`, `@Setter`, etc.).
- No incluir lógica de negocio ni validaciones en los `records`.
- Mantener solo estructura de datos para transporte hacia/desde servicios externos.

## Ubicación y package
- Package base obligatorio: `com.pichincha.sp.infrastructure.output.adapter`.
- Subpackages obligatorios:
  - `com.pichincha.sp.infrastructure.output.adapter.request`
  - `com.pichincha.sp.infrastructure.output.adapter.response`

## Convenciones de nombre
- Request por invocación: `<Servicio><Operacion>Request`
- Response por invocación: `<Servicio><Operacion>Response`
- Si no se conoce operación exacta:
  - `<Servicio>Request`
  - `<Servicio>Response`

## Tipado y estructura
- Inferir tipos Java desde WSDL/XSD y levantamiento.
- Para colecciones, usar `List<...>` y crear `record` anidado cuando aplique.
- Modelar de forma recursiva completa los `record` anidados (sin omitir niveles intermedios).
- Si aparece `List<...>`, declarar explícitamente el `record` item con sus campos.
- Si un tipo no está confirmado, usar `String` y marcar:
  - `// TODO: verificar tipo en contrato dependiente`

## Snippet reutilizable
- Para una regla corta reusable en prompts de generación, usar:
  - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`

## Plantilla base
```java
package com.pichincha.sp.infrastructure.output.adapter.request;

import java.util.List;

public record WsClientes0055Request(
        String numeroDocumento,
        List<DetalleItem> detalles
) {
    public record DetalleItem(
            String codigo,
            String valor
    ) {}
}
```

```java
package com.pichincha.sp.infrastructure.output.adapter.response;

public record WsClientes0055Response(
        String codigoRespuesta,
        String mensaje
) {}
```

## Alcance
Aplicable a modelos request/response generados para adapters en:
- `com.pichincha.sp.infrastructure.output.adapter`

```
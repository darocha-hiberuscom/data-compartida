# PROMPT BASE — EVIDENCIA Y TRAZABILIDAD

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar cómo documentar evidencia técnica y vacíos de información durante el levantamiento y análisis.

## Reglas obligatorias
- NO inventar información técnica no evidenciada.
- Toda afirmación relevante debe incluir evidencia trazable (archivo/ruta/fragmento técnico).
- Cuando no exista evidencia suficiente, marcar explícitamente:
  - `FALTA DETALLE`
  - `Pista: <archivo/ruta/comando sugerido>`
- La pista debe ser accionable y específica (no genérica).
- Mantener trazabilidad entre artefactos: cada hallazgo debe poder rastrearse a su origen técnico.

## Formato mínimo sugerido de evidencia
- Archivo o recurso fuente
- Ubicación o contexto técnico
- Hallazgo asociado
- Estado (`Evidenciado` / `FALTA DETALLE`)

## Plantilla reusable — Evidencia ESQL

Usar esta plantilla cuando la evidencia proviene de `.esql` y se reporta en tablas o bullets.

### Formato estándar
- `L<numero> // <texto exacto de la línea ESQL>`

Ejemplos:
- `L185 // CALL InvocarCatalogoSubSectorEconomicoBancs() INTO respuesta;`
- `L325 // IF service.error.codigo = '0' THEN`
- `L156 // SET Environment.salida.error.codigo = '10';`

### Regla para columnas de evidencia
- Si la columna se llama `Línea/evidencia`, `Evidencia` o similar, incluir siempre:
  1. Número de línea (`L<numero>`)
  2. Comentario con texto exacto de la sentencia ESQL

### Fallback cuando no exista línea exacta
- Si no se puede determinar línea exacta, usar:
  - `FALTA DETALLE`
  - `Pista: <archivo .esql y bloque/procedimiento a revisar>`

# PROMPT BASE — EVIDENCIA Y TRAZABILIDAD

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

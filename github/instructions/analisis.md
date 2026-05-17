```markdown
# Instrucción — Análisis de Orquestadores (Pasos 1 a 6)

## Alcance
Aplicable a todos los pasos del levantamiento de orquestadores:
- Paso 1: Visión general
- Paso 2: Contratos WSDL/XSD
- Paso 3: Análisis ESQL (flujos, validaciones, orquestación, pseudocódigo)
- Paso 4: Mapeo de APIs invocadas
- Paso 5: Mapeo entrada/salida del orquestador y consolidado de errores
- Paso 6: Resumen de servicios, DTOs y JSON request

## Reglas
- No modificar código fuente del proyecto legado.
- No inventar información; toda afirmación debe tener evidencia.
- Si falta evidencia: usar `FALTA DETALLE` + `Pista: <archivo/ruta sugerido>`.
- Mantener trazabilidad por archivo/ruta/línea analizada.
- Respetar la estructura de salida definida por cada prompt.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`.

## Plantilla rápida de evidencia ESQL
- Cuando cites evidencia desde `.esql`, incluir siempre número de línea + texto exacto de la sentencia.
- Formato estándar: `L<numero> // <texto exacto de la línea ESQL>`
- Ejemplo: `L185 // CALL InvocarCatalogoSubSectorEconomicoBancs() INTO respuesta;`
- Si no se puede identificar línea exacta: `FALTA DETALLE` + `Pista: <archivo/procedimiento>`.

## Regla de idioma para código
- Si se genera pseudocódigo o fragmentos de código, deben estar en inglés.

## Regla de runtime para scripts auxiliares
- Si durante el análisis se requieren scripts de apoyo (búsqueda, extracción, consolidación), implementarlos en **Node.js**.
- Evitar Python en este flujo, porque los ambientes objetivo pueden no tener Python instalado.
- Priorizar ejecución con `node` y librerías del ecosistema JavaScript cuando sea necesario automatizar.

## Fuentes de datos
- Código ESQL: `repos/origen/proyecto-legado/`
- Contratos XML/SOAP: `repos/origen/servicio-dependencia/`

## Criterio de salida
- Documento generado en `./migracion/LEVANTAMIENTO_INFORMACION.md` con cobertura completa del paso.
- Cada paso debe validar su checklist antes de considerarse terminado.

```

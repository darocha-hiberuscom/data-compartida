# PASO 4 — MAPEO DE APIs / SERVICIOS INVOCADOS

> Nota: este archivo corresponde a un paso ejecutable del proceso de levantamiento.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## PREREQUISITO
- Paso 3 completado (secciones 4.1 a 4.6 del documento generadas).

## OBJETIVO
Por cada API/servicio invocado desde el ESQL, documentar el mapeo completo de campos de entrada (con valores fijos), salida, paths XML SOAP y manejo de respuesta.

## ALCANCE
- Cubre la sección 4.7 del documento.
- Analiza cada procedimiento `CREATE PROCEDURE Invocar<NombreServicio>() RETURNS BOOLEAN` en el ESQL.
- Cruza con los XML SOAP de `repos/origen/servicios-dependencia`.

## INSTRUCCIONES

### Por cada procedimiento `Invocar*()`:

1. **Identificar el servicio**: nombre del procedimiento → nombre del servicio.
2. **Obtener URL/Endpoint**: buscar en `repos/origen/servicios-dependencia` el archivo con el nombre del servicio.
3. **Mapeo de entrada**: detectar asignaciones tipo:
   - `SET requestField = bodyIn.<ruta.campo>;` → campo mapeado desde entrada
   - `SET requestField = Environment.<ruta>;` → campo mapeado desde entorno
   - `SET requestField = '<valor_fijo>';` → valor fijo (hardcode)
4. **Mapeo de salida**: detectar lecturas de la respuesta del servicio.
5. **Path XML SOAP**: los XML de origen y destino están en `repos/origen/servicios-dependencia`:
   - **Origen**: archivo con el nombre del orquestador → path XPath del campo.
   - **Destino**: archivo con el nombre del servicio invocado → path XPath del campo.
6. **Manejo de respuesta**: detectar el patrón:
   - Éxito: `IF service.error.codigo = '0' THEN RETURN TRUE;`
   - Error: `SET Environment.salida.error = service.error; RETURN FALSE;`

### Formato de salida por API:

```markdown
##### API: `<NombreProcedimiento>` → Servicio: `<nombre_servicio>`
- URL/Endpoint: (extraído de servicios-dependencia)
- Método: (POST/GET/PUT, si aplica)

Tabla de mapeo de entrada:
| Campo request (servicio) | Origen (campo bodyIn / Environment) | Valor fijo (si aplica) | Tipo | Path XML SOAP origen | Path XML SOAP destino | Línea/evidencia |

Tabla de mapeo de salida:
| Campo respuesta (servicio) | Destino (campo Environment / salida) | Tipo | Path XML SOAP origen (response) | Path XML SOAP destino (Environment) | Línea/evidencia |

Manejo de respuesta:
| Resultado | Condición | Acción | Línea/evidencia |
```

## FUENTES
- `repos/origen/proyecto-legado/*.esql`
- `repos/origen/servicios-dependencia/*`

## SALIDA OBLIGATORIA
Agregar al archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` la sección 4.7.

## CHECKLIST DE VALIDACIÓN
- [ ] Todos los procedimientos `Invocar*()` del ESQL documentados
- [ ] URL/Endpoint obtenido de servicios-dependencia para cada servicio
- [ ] Mapeo de entrada completo (campos mapeados + valores fijos)
- [ ] Mapeo de salida documentado
- [ ] Paths XML SOAP origen y destino completados
- [ ] Manejo de respuesta (éxito/error) documentado por servicio
- [ ] Transformaciones de campos identificadas (TRIM, COALESCE, etc.)
- [ ] Si falta un XML en servicios-dependencia, marcado como `FALTA DETALLE`

## REGLAS
- NO modificar código fuente.
- NO inventar información.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRAZABILIDAD.md`.

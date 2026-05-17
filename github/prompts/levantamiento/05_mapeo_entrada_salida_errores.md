# PASO 5 — MAPEO ENTRADA/SALIDA DEL ORQUESTADOR Y CONSOLIDADO DE ERRORES

> Nota: este archivo corresponde a un paso ejecutable del proceso de levantamiento.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## PREREQUISITO
- Paso 4 completado (sección 4.7 del documento generada).

## OBJETIVO
Documentar el mapeo completo de entrada y salida del orquestador (qué recibe y qué devuelve) y consolidar todos los mensajes de error detectados en el ESQL.

## ALCANCE
- Cubre las secciones 4.8, 4.9 y 4.10 del documento.

## INSTRUCCIONES

### Regla de URL del servicio a invocar (fallback)
1. Intentar detectar la URL del servicio en fuentes directas (`repos/origen/servicio-dependencia/*.txt`, ESQL, o artefactos de configuración).
2. Si no se detecta URL, usar por defecto el patrón:
   - `https://tnd-msa-sp-{servicio-minusculas}-enp.apps.ocpdev.uiotest.bpichinchatest.test/IntegrationBus/soap/{ServicioCamelCase}`
3. Construir `{servicio-minusculas}` y `{ServicioCamelCase}` con el nombre del servicio objetivo (por ejemplo, `wsclientes0055` y `WSClientes0055`).
4. Registrar la URL como evidencia **inferida** cuando se aplique este fallback.

### 4.8 Mapeo de entrada del orquestador
1. Documentar todos los campos que recibe el orquestador como entrada (request).
2. Obtener el path del request en el XML SOAP del orquestador desde `repos/origen/servicio-dependencia` (archivo con el nombre del orquestador) como fuente principal.
3. Solo si no se encuentra información en la carpeta de servicios legados/dependencia, inferir la ruta completa del campo en `bodyIn` desde el código ESQL.
4. Cuando se aplique inferencia, apoyar el análisis con la estructura general observada en los otros servicios; marcar explícitamente el registro como **inferido** y dejar la evidencia usada.

| Campo (ruta bodyIn) | Tipo | Path XML SOAP (request orquestador) | Descripción / Uso | Tipo de evidencia (directa/inferida) |
|----------------------|------|--------------------------------------|-------------------|---------------------------------------|

### 4.9 Mapeo de salida del orquestador
1. Documentar todos los campos que devuelve el orquestador como salida (response).
2. Inferir la ruta completa en `Environment.salida` desde el código ESQL (fuente principal).
3. Indicar el origen de cada dato (qué servicio, validación o valor fijo lo genera).
4. Contrastar/validar el path de response contra los insumos de `repos/origen/servicio-dependencia` cuando existan.
5. Si no se encuentra información del servicio de dependencia, inferir el path/campo usando el código ESQL y la estructura general observada en los otros servicios; marcar explícitamente el registro como **inferido** y dejar la evidencia usada.

| Campo (ruta Environment.salida) | Tipo | Path XML SOAP (response orquestador) | Origen del dato (servicio / validación / fijo) | Tipo de evidencia (directa/inferida) |
|----------------------------------|------|---------------------------------------|------------------------------------------------|---------------------------------------|

### 4.10 Consolidado de mensajes de error
1. Recorrer TODO el ESQL y recopilar cada `SET Environment.salida.error.codigo` y `SET Environment.salida.error.mensaje`.
2. Incluir errores de:
   - Validaciones de entrada (`ValidarEntrada`)
   - Orquestación de servicios (`OrquestarWS`)
   - Respuestas de servicios invocados (propagación de `service.error`)
   - Cualquier otro `SET Environment.salida.error.*`
3. Incluir errores propagados desde servicios: `SET Environment.salida.error = service.error`.
4. Si se detectan bloques TRY/CATCH, documentarlos.

| # | Código error | Mensaje de error | Tipo | Origen (procedimiento/función) | Condición que lo genera | Línea/evidencia (Línea + comentario con texto ESQL) |
|---|--------------|------------------|------|-------------------------------|-------------------------|-----------------|

Reglas del consolidado:
- Ordenar por código de error de menor a mayor.
- Para errores propagados, indicar el procedimiento origen y que el error viene de `service.error.codigo != '0'`.
- No omitir ningún error.

## FUENTES
- `repos/origen/proyecto-legado/*.esql`
- `repos/origen/servicio-dependencia/*` (XML/insumo del orquestador)

## SALIDA OBLIGATORIA
Agregar al archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` las secciones 4.8, 4.9 y 4.10.

## CHECKLIST DE VALIDACIÓN
- [ ] Todos los campos de entrada del orquestador documentados (4.8)
- [ ] Request obtenido desde servicio-dependencia o inferido desde ESQL solo cuando no exista en esa carpeta
- [ ] Path XML SOAP del request del orquestador completados
- [ ] URL del servicio detectada o construida con fallback y marcada como evidencia (directa/inferida)
- [ ] Todos los campos de salida del orquestador documentados (4.9)
- [ ] Response inferido desde ESQL y contrastado con servicio-dependencia cuando exista
- [ ] Origen de cada dato de salida identificado
- [ ] Todas las filas de 4.8 y 4.9 con tipo de evidencia completado (directa/inferida)
- [ ] Todos los mensajes de error del ESQL consolidados (4.10)
- [ ] Errores de validación incluidos
- [ ] Errores de orquestación incluidos
- [ ] Errores propagados desde servicios incluidos
- [ ] Tabla ordenada por código de error

## REGLAS
- NO modificar código fuente.
- Request: usar como fuente principal `repos/origen/servicio-dependencia`; inferir desde ESQL solo cuando no exista información en esa carpeta.
- Response: levantar desde ESQL como fuente principal; usar servicio-dependencia para contraste cuando exista.
- NO inventar información; si faltan datos del servicio de dependencia, usar inferencia trazable desde ESQL + patrón de otros servicios y marcarlo como inferido.
- En cada `Línea/evidencia`, incluir `L<numero>` y el texto exacto de la línea ESQL como comentario breve.
- Formato sugerido: `L156 // SET Environment.salida.error.codigo = '10';`
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`.

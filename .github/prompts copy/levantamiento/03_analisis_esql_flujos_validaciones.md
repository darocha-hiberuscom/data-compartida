# PASO 3 — ANÁLISIS ESQL: FLUJOS, VALIDACIONES Y ORQUESTACIÓN

> Nota: este archivo corresponde a un paso ejecutable del proceso de levantamiento.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## PREREQUISITO
- Paso 2 completado (secciones 2 y 3 del documento generadas).

## OBJETIVO
Analizar en profundidad los archivos ESQL para documentar flujos, validaciones de entrada, estructura del Main, secuencia de orquestación, lógica de negocio por flujo y pseudocódigo.

## ALCANCE
- Archivos `.esql` en `repos/origen/proyecto-legado`.
- Cubre las secciones 4.1 a 4.6 del documento.

## INSTRUCCIONES

### 4.1 Flujos del servicio
1. Buscar la referencia: `DECLARE flujoIn REFERENCE TO Environment.entrada.bodyIn.flujo;`
2. Identificar cada condicional `IF flujoIn.estado = '<valor>' THEN`.
3. Cada valor distinto de `flujoIn.estado` es un flujo del servicio.

| Flujo (valor de flujoIn.estado) | Línea/evidencia | Descripción funcional inferida |
|---------------------------------|-----------------|-------------------------------|

### 4.2 Validaciones de campos
1. Buscar procedimientos tipo `CREATE PROCEDURE ValidarEntrada() RETURNS BOOLEAN`.
2. Detectar validaciones con patrón: `IF LENGTH(TRIM(COALESCE(bodyIn.<campo>,''))) <= 0 THEN`
3. Detectar validaciones numéricas: `IF LENGTH(TRIM(COALESCE(bodyIn.<campo>,'0'))) <= 0 THEN`
4. Detectar errores: `SET Environment.salida.error.codigo = '<código>';` + mensaje + tipo.
5. Detectar transformaciones de valores (ejemplo: 'SI'→'S', 'NO'→'N').
6. Detectar llamadas a procedimientos auxiliares dentro de la validación.

| Campo (ruta completa) | Tipo | Validación | Código error | Mensaje de error | Acción adicional (si aplica) | Línea/evidencia |
|------------------------|------|------------|--------------|------------------|------------------------------|-----------------|

### 4.3 Flujo principal — Main
1. Buscar `CREATE FUNCTION Main() RETURNS BOOLEAN`.
2. Documentar la secuencia: `CALL ValidarEntrada()` → `CALL OrquestarWS()`.
3. Documentar respuesta exitosa: `codigo = '0'`, `mensaje = 'OK'`.

| Condición | Procedimiento origen | Código respuesta | Mensaje respuesta | Resultado (RETURN) |
|-----------|---------------------|------------------|-------------------|--------------------|

### 4.4 Orquestación de servicios — Secuencia por flujo
1. Buscar `CREATE PROCEDURE OrquestarWS() RETURNS BOOLEAN`.
2. Documentar que la orquestación se agrupa por `IF flujoIn.estado = '<estado>' THEN`.
3. Dentro de cada flujo, listar secuencia de `CALL Invocar*()`.
4. Detectar reintentos, condiciones especiales (`IF EXISTS`, `IF campo IN`, `IF flag`).
5. Detectar errores directos en orquestación.

| # Orden | Servicio invocado (procedimiento) | Flujo (estado) | Condición de ejecución | Reintento (SI/NO) | Error si falla (origen) | Línea/evidencia |
|---------|-----------------------------------|----------------|------------------------|--------------------|-------------------------|-----------------|

Tabla de errores directos:
| Condición | Código error | Mensaje de error | Línea/evidencia |
|-----------|--------------|------------------|-----------------|

### 4.5 Lógica de negocio por estado de flujo
Por cada flujo detectado, generar tabla resumen:

| # Paso | Acción | Condición previa | Servicio invocado | Si falla | Si éxito |
|--------|--------|------------------|-------------------|----------|----------|

### 4.6 Pseudocódigo del flujo completo
Generar pseudocódigo legible que consolide validaciones + orquestación:

```
FUNCIÓN Main():
  SI ValidarEntrada() falla → RETORNAR error
  SI OrquestarWS() falla → RETORNAR error
  RETORNAR código=0, mensaje="OK"

PROCEDIMIENTO ValidarEntrada():
  SI campo_1 vacío → error código=1, mensaje="..."
  ...

PROCEDIMIENTO OrquestarWS():
  SI flujo.estado = "iniciar":
    1. Llamar Servicio_1 ...
    ...
```

Reglas del pseudocódigo:
- Fiel al ESQL pero simplificado para lectura humana.
- Incluir condiciones especiales, reintentos y dependencias.
- No omitir ningún servicio ni validación.

## FUENTES
- `repos/origen/proyecto-legado/*.esql`

## SALIDA OBLIGATORIA
Agregar al archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` la sección 4 (puntos 4.1 a 4.6).

## CHECKLIST DE VALIDACIÓN
- [ ] Todos los flujos (`flujoIn.estado`) identificados (4.1)
- [ ] Todas las validaciones de campos documentadas con código y mensaje de error (4.2)
- [ ] Flujo Main documentado con respuestas OK/error (4.3)
- [ ] Secuencia de orquestación por estado de flujo completa (4.4)
- [ ] Reintentos identificados
- [ ] Condiciones especiales de ejecución documentadas
- [ ] Lógica de negocio por flujo tabulada (4.5)
- [ ] Pseudocódigo generado con todos los servicios y validaciones (4.6)
- [ ] No se inventó información — todo tiene evidencia con línea/archivo

## REGLAS
- NO modificar código fuente.
- NO inventar información.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRAZABILIDAD.md`.

# PASO 1 — VISIÓN GENERAL DEL ORQUESTADOR

> Nota: este archivo corresponde a un paso ejecutable del proceso de levantamiento.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## PREREQUISITO
- Sin prerrequisitos. Este es el primer paso ejecutable.

## OBJETIVO
Identificar la visión general del proyecto orquestador y los servicios principales que invoca, generando la sección inicial del documento de levantamiento.

## ALCANCE
- Analizar los archivos `.esql` en `repos/origen/proyecto-legado` para identificar todos los servicios que utiliza el orquestador.
- Cada procedimiento tipo `InvocarXxx()` o `CALL Invocar*()` encontrado en el ESQL representa un servicio integrado.

## INSTRUCCIONES
1. Recorrer todos los archivos `.esql` en `repos/origen/proyecto-legado`.
2. Identificar el nombre del proyecto a partir del nombre del workspace o carpeta raíz.
3. Listar todos los procedimientos de invocación de servicios (`CREATE PROCEDURE Invocar*` y `CALL Invocar*`).
4. Cada servicio identificado es un componente principal del orquestador.

## FUENTES
- `repos/origen/proyecto-legado/*.esql`

## SALIDA OBLIGATORIA
Generar (o inicializar) el archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` con la siguiente sección:

```markdown
## 1. VISIÓN GENERAL
- Nombre del proyecto: <nombre>
- Tipo de solución global: ORQUESTADOR
- Componentes principales (servicios identificados):
  - <servicio_1>
  - <servicio_2>
  - ...
```

## CHECKLIST DE VALIDACIÓN
- [ ] Archivo `LEVANTAMIENTO_INFORMACION.md` creado en `./migracion/`
- [ ] Nombre del proyecto identificado
- [ ] Tipo de solución = ORQUESTADOR
- [ ] Todos los procedimientos `Invocar*()` del ESQL listados como componentes principales
- [ ] No se inventó información — todo tiene evidencia del código fuente

## REGLAS
- NO modificar código fuente.
- NO inventar información.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRAZABILIDAD.md`.

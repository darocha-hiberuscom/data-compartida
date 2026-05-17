# PASO 6 — RESUMEN GLOBAL DE SERVICIOS, DTOs Y JSON REQUEST

> Nota: este archivo corresponde a un paso ejecutable del proceso de levantamiento.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## PREREQUISITO
- Paso 5 completado (secciones 4.8, 4.9 y 4.10 del documento generadas).

## OBJETIVO
Generar un resumen consolidado de todos los servicios invocados por el orquestador, modelar los DTOs de entrada y salida por servicio, y producir un JSON request de ejemplo por cada servicio.

## ALCANCE
- Cubre las secciones 12, 13 y 14 del documento.

## INSTRUCCIONES

### 12. Resumen global de servicios invocados

Generar una tabla consolidada con TODOS los servicios invocados por el orquestador:

| # | Nombre del servicio | Procedimiento ESQL que lo invoca | Flujo(s) donde se usa | Tipo (SOAP/REST/MQ) | URL / Endpoint | Resumen de función |
|---|---------------------|----------------------------------|----------------------|----------------------|----------------|-------------------|

Reglas:
- Incluir TODOS los servicios detectados en el paso 4 (sección 4.7).
- Indicar en qué flujo(s) / estado(s) se invoca cada servicio.
- Si un servicio se invoca en múltiples flujos, listar todos separados por coma.
- El resumen debe ser una frase concisa de qué hace el servicio en el contexto del orquestador.

### 13. DTOs de entrada y salida por servicio

Para CADA servicio del resumen (sección 12), generar la especificación de sus DTOs:

#### Servicio: `<NombreServicio>`

**DTO Request:**
| Campo | Tipo Java | Obligatorio | Origen (campo bodyIn / valor fijo / transformación) | Descripción |
|-------|-----------|-------------|------------------------------------------------------|-------------|

**DTO Response:**
| Campo | Tipo Java | Descripción | Destino (campo Environment.salida) |
|-------|-----------|-------------|-------------------------------------|

Reglas:
- Inferir tipos Java a partir de los tipos XSD / WSDL (paso 2) y del uso en ESQL.
  - `xs:string` → `String`
  - `xs:int`, `xs:integer` → `Integer` o `Long`
  - `xs:decimal` → `BigDecimal`
  - `xs:date` → `LocalDate`
  - `xs:dateTime` → `LocalDateTime`
  - `xs:boolean` → `Boolean`
- Si un campo es una colección (se itera con `WHILE` o `MOVE`), indicar `List<NombreDTO>` y definir el DTO anidado.
- Para colecciones, agregar una sub-tabla:

**Colección: `<nombreColeccion>` → `List<NombreItemDTO>`**
| Campo | Tipo Java | Descripción |
|-------|-----------|-------------|

- Si no se puede determinar el tipo exacto, usar `String` como default y marcar `// TODO: verificar tipo`.

### 14. JSON request de ejemplo por servicio

Para CADA servicio del resumen (sección 12), generar un JSON de ejemplo con datos representativos:

```json
// Servicio: <NombreServicio>
// Procedimiento: <NombreProcedimiento>
{
  "campo1": "valor_ejemplo",
  "campo2": 123,
  "coleccion": [
    {
      "subcampo1": "valor",
      "subcampo2": true
    }
  ]
}
```

Reglas:
- Usar valores de ejemplo coherentes con el tipo de dato y la descripción del campo.
- Para campos de identificación, usar valores ficticios representativos (ej: `"1234567890"`).
- Para campos con valores fijos detectados en el ESQL, usar esos valores exactos.
- Incluir la estructura completa (incluyendo colecciones con al menos un elemento).
- Agregar comentarios indicando el nombre del servicio y procedimiento.

## FUENTES
- Secciones 4.7, 4.8, 4.9 y 4.10 ya generadas en `./migracion/LEVANTAMIENTO_INFORMACION.md`
- `repos/origen/proyecto-legado/*.esql`
- `repos/origen/servicios-dependencia/*`
- Contratos WSDL/XSD documentados en secciones 2 y 3

## SALIDA OBLIGATORIA
Agregar al archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` las secciones 12, 13 y 14.

## CHECKLIST DE VALIDACIÓN
- [ ] Tabla resumen con todos los servicios invocados (sección 12)
- [ ] Flujo(s) de uso indicado por servicio
- [ ] DTO Request generado por cada servicio (sección 13)
- [ ] DTO Response generado por cada servicio (sección 13)
- [ ] Tipos Java inferidos correctamente desde XSD/WSDL
- [ ] Colecciones identificadas con su DTO anidado
- [ ] JSON request de ejemplo por cada servicio (sección 14)
- [ ] Valores fijos del ESQL usados en los JSON de ejemplo
- [ ] Todos los servicios de la sección 12 tienen su DTO y JSON

## REGLAS
- NO modificar código fuente.
- NO inventar información.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRAZABILIDAD.md`.

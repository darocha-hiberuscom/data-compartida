# PASO 6 — RESUMEN GLOBAL DE SERVICIOS, DTOs, CLASES CANDIDATAS Y JSON REQUEST

> Nota: este archivo corresponde a un paso ejecutable del proceso de levantamiento.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## PREREQUISITO
- Paso 5 completado (secciones 4.8, 4.9 y 4.10 del documento generadas).

## OBJETIVO
Generar un resumen consolidado de todos los servicios invocados por el orquestador, modelar los DTOs de entrada y salida por servicio, generar una matriz consolidada de campos de entrada/salida por servicio, definir clases candidatas por servicio (desde WSDL + request/response de servicios) y producir un JSON request de ejemplo por cada servicio.

## ALCANCE
- Cubre las secciones 12, 13, 14 y 15 del documento.

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

Convención obligatoria de sección 13:
- Expresar `Campo ... (ruta DTO)` en formato `dot.path`.
- No usar sufijo `[]` en la ruta del campo; representar colecciones solo mediante el tipo `List<...>`.
- Usar sufijo `DTO` de forma consistente para tipos anidados.
- Si el tipo no es concluyente, usar tipo conservador y marcar `// TODO: verificar tipo`.

### 13.1 Matriz consolidada — Campos de entrada por servicio (OBLIGATORIO)

Generar una tabla única con granularidad campo-a-campo para TODOS los servicios de la sección 12:

| Servicio | Operación | Campo de entrada (ruta DTO) | Tipo Java | Obligatorio | Origen (bodyIn/Environment/fijo/transformación) | Evidencia (línea/sección + comentario ESQL cuando aplique) |
|----------|-----------|------------------------------|-----------|-------------|--------------------------------------------------|---------------------------|

Reglas:
- Debe incluir todos los campos de entrada identificados por servicio.
- Si hay colecciones, incluir cada campo interno relevante con ruta `dot.path` y tiparlo como `List<...>`.
- Si un tipo no está confirmado, usar `String` y marcar `TODO: verificar tipo`.
- Si la evidencia proviene de ESQL, incluir `L<numero>` y el texto exacto de la línea como comentario breve.

### 13.2 Matriz consolidada — Campos de salida por servicio (OBLIGATORIO)

Generar una tabla única con granularidad campo-a-campo para TODOS los servicios de la sección 12:

| Servicio | Operación | Campo de salida (ruta DTO) | Tipo Java | Destino (Environment.salida / tempDatos / otro) | Origen de respuesta (bodyOut/error/header) | Evidencia (línea/sección + comentario ESQL cuando aplique) |
|----------|-----------|-----------------------------|-----------|--------------------------------------------------|--------------------------------------------|---------------------------|

Reglas:
- Debe incluir todos los campos de salida identificados por servicio, incluyendo `error.*` cuando aplique.
- Si un servicio solo expone control de error, documentar explícitamente `error.codigo`, `error.mensaje`, `error.tipo` cuando exista evidencia.
- Si no se evidencia `bodyOut`, registrar `FALTA DETALLE` sin omitir la fila del servicio.
- Si la evidencia proviene de ESQL, incluir `L<numero>` y el texto exacto de la línea como comentario breve.

### 13.3 Detalle DTO por servicio

Para CADA servicio del resumen (sección 12), generar la especificación de sus DTOs:

Reglas de cobertura total (OBLIGATORIO):
- Debe existir exactamente un bloque `#### Servicio: <NombreServicio>` por cada fila de la sección 12.
- El total de bloques de 13.3 debe ser igual al total de servicios de la sección 12.
- PROHIBIDO agrupar servicios en secciones tipo `Servicio: RESTO`, `Servicio: OTROS`, `Servicios pendientes` o similares.
- Si faltan campos/evidencia para un servicio, mantener el bloque del servicio y registrar `FALTA DETALLE` + `Pista` dentro de ese mismo servicio.
- No se permite omitir un servicio en 13.3 aunque su información sea parcial.

#### Servicio: `<NombreServicio>`

**DTO Request:**
| Campo | Tipo Java | Obligatorio | Origen (campo bodyIn / valor fijo / transformación) | Descripción |
|-------|-----------|-------------|------------------------------------------------------|-------------|

**DTO Response:**
| Campo | Tipo Java | Descripción | Destino (campo Environment.salida) |
|-------|-----------|-------------|-------------------------------------|

**Desglose recursivo completo (OBLIGATORIO):**
- Después de las tablas de `DTO Request` y `DTO Response`, agregar una sección llamada:
  - `Desglose recursivo completo (sin omitir clases internas)`
- Esta sección debe listar en formato árbol TODAS las clases internas/anidadas, sin omitir niveles intermedios.
- Debe incluir:
  - Clase raíz request/response
  - Todas las clases anidadas directas
  - Todas las clases anidadas de clases anidadas (nietas, bisnietas, etc.)
  - Colecciones como `List<...DTO>` con su clase item definida
- Si una clase anidada no tiene campos evidenciados, registrar:
  - `FALTA DETALLE`
  - `Pista: <archivo/procedimiento donde completar>`

Formato obligatorio del árbol:

```markdown
#### Servicio: <NombreServicio>
**DTO Request:** <NombreRequestDTO>
- <campoRaiz>: <TipoDTO>

**Desglose recursivo completo (sin omitir clases internas):**
- <NombreRequestDTO>
  - <campo>: <TipoDTO>
- <TipoDTO>
  - <campoHijo>: <TipoHijoDTO>
- <TipoHijoDTO>
  - <lista>: List<<TipoItemDTO>>
- <TipoItemDTO>
  - <campoSimple>: <TipoJava>

**DTO Response:** <NombreResponseDTO>
- error: ErrorResponseGeneralDTO
```

Reglas:
- Inferir tipos Java a partir de los tipos XSD / WSDL (paso 2) y del uso en ESQL.
  - `xs:string` → `String`
  - `xs:int`, `xs:integer` → `Integer` o `Long`
  - `xs:decimal` → `BigDecimal`
  - `xs:date` → `LocalDate`
  - `xs:dateTime` → `LocalDateTime`
  - `xs:boolean` → `Boolean`
- Si un campo es una colección (se itera con `WHILE` o `MOVE`), indicar `List<NombreDTO>` y definir el DTO anidado.
- El detalle DTO debe ser recursivo completo: no se permite dejar solo la primera clase anidada.
- No usar rutas parciales tipo `a.b.c` como sustituto de clases intermedias; cada nivel debe materializar su clase DTO.
- Para colecciones, agregar una sub-tabla:

**Colección: `<nombreColeccion>` → `List<NombreItemDTO>`**
| Campo | Tipo Java | Descripción |
|-------|-----------|-------------|

- Si no se puede determinar el tipo exacto, usar `String` como default y marcar `// TODO: verificar tipo`.
- Las tablas de detalle por servicio deben ser consistentes 1:1 con las matrices consolidadas 13.1 (entrada) y 13.2 (salida).
- Las matrices 13.1 y 13.2 son la fuente principal para el paso de implementación de DTOs.
- `DTO Response` también debe desglosarse recursivamente cuando tenga `bodyOut`, `headerOut` o estructuras anidadas; si no existe evidencia de `bodyOut`, registrar explícitamente `FALTA DETALLE`.
- Validación obligatoria de completitud en 13.3:
  - `#bloques_servicio_13.3 == #servicios_seccion_12`
  - Si no cumple, corregir antes de finalizar el documento.

### 14. JSON request de ejemplo por servicio

Para CADA servicio del resumen (sección 12), generar un JSON de ejemplo con datos representativos:

Reglas de cobertura total (OBLIGATORIO):
- Debe existir un JSON de ejemplo por cada servicio de la sección 12.
- PROHIBIDO agrupar JSONs en bloques tipo `RESTO`/`OTROS`.
- Si falta evidencia de campos, generar el JSON mínimo del servicio y marcar `FALTA DETALLE` + `Pista` dentro del bloque de ese servicio.

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

### 15. Clases candidatas por servicio (OBLIGATORIO)

Generar una sección de clases candidatas para implementación Java, por cada servicio de la sección 12, tomando como base obligatoria:
1. Request/Response del WSDL/XSD del orquestador (secciones 2 y 3).
2. Request/Response de los servicios de dependencia (`repos/origen/servicio-dependencia` y/o mapeo de sección 4.7).

Formato de salida:

```markdown
## 15. CLASES CANDIDATAS

### Servicio: `<NombreServicio>`
| Clase candidata | Tipo (Request/Response/Anidada/Error) | Origen (WSDL/XSD orquestador / Servicio dependencia / ESQL) | Campos principales (resumen) | Observaciones |
|-----------------|-----------------------------------------|---------------------------------------------------------------|-------------------------------|--------------|

```

Reglas:
- Definir clases candidatas de request y response por servicio con sufijo `DTO`.
- Incluir clases anidadas para estructuras complejas o colecciones (`List<...DTO>`).
- El inventario de clases candidatas debe ser recursivo completo por servicio (todas las clases internas en todos los niveles).
- Si se referencia una clase anidada en un campo, esa clase debe aparecer explícitamente como fila en la tabla de la sección 15.
- No se acepta salida resumida incompleta (ejemplo inválido: solo `RequestDTO -> ADTO -> List<BDTO>` sin declarar `ADTO` y `BDTO` con sus campos).
- Si falta evidencia de un campo/tipo, registrar `FALTA DETALLE` y `Pista`.
- No inventar atributos: solo usar campos evidenciados en WSDL/XSD, ESQL y request/response de servicios.

Regla obligatoria de errores en clases candidatas:
- No crear una clase de error distinta por servicio.
- Definir una única clase general de error reutilizable para todos los servicios:

```markdown
Clase candidata obligatoria:
`ErrorResponseGeneralDTO`

Campos mínimos:
- `codigo: String`
- `mensaje: String`
- `tipo: String`

Campos opcionales (si existe evidencia):
- `recurso: String`
- `componente: String`
- `backend: String`
```

## FUENTES
- Secciones 4.7, 4.8, 4.9 y 4.10 ya generadas en `./migracion/LEVANTAMIENTO_INFORMACION.md`
- `repos/origen/proyecto-legado/*.esql`
- `repos/origen/servicio-dependencia/*`
- Contratos WSDL/XSD documentados en secciones 2 y 3

## SALIDA OBLIGATORIA
Agregar al archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` las secciones 12, 13, 14 y 15.

## CHECKLIST DE VALIDACIÓN
- [ ] Tabla resumen con todos los servicios invocados (sección 12)
- [ ] Flujo(s) de uso indicado por servicio
- [ ] Matriz consolidada de campos de entrada por servicio (sección 13.1)
- [ ] Matriz consolidada de campos de salida por servicio (sección 13.2)
- [ ] DTO Request generado por cada servicio (sección 13)
- [ ] DTO Response generado por cada servicio (sección 13)
- [ ] Desglose recursivo completo de DTOs por servicio (sin omitir clases internas)
- [ ] Cobertura total 13.3: número de bloques `#### Servicio:` coincide exactamente con total de servicios de sección 12
- [ ] No existen bloques agregados tipo `RESTO` / `OTROS` en 13.3
- [ ] Tipos Java inferidos correctamente desde XSD/WSDL
- [ ] Colecciones identificadas con su DTO anidado
- [ ] JSON request de ejemplo por cada servicio (sección 14)
- [ ] Cobertura total 14: cantidad de JSONs coincide exactamente con total de servicios de sección 12
- [ ] No existen bloques agregados tipo `RESTO` / `OTROS` en sección 14
- [ ] Valores fijos del ESQL usados en los JSON de ejemplo
- [ ] Todos los servicios de la sección 12 tienen su DTO y JSON
- [ ] Coherencia entre matrices 13.1/13.2 y detalle DTO por servicio
- [ ] Sección de clases candidatas generada (sección 15)
- [ ] Clases candidatas definidas usando WSDL/XSD + request/response de servicios
- [ ] Todas las clases internas/anidadas listadas explícitamente en sección 15
- [ ] Clase única `ErrorResponseGeneralDTO` incluida y reutilizable

## REGLAS
- NO modificar código fuente.
- NO inventar información.
- PROHIBIDO resumir servicios bajo etiquetas agrupadas (`RESTO`, `OTROS`, `PENDIENTES`).
- Cada servicio identificado debe aparecer explícitamente en secciones 13.3, 14 y 15.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_DTO_RECORDS.md` para modelado recursivo completo de DTOs/clases internas.
- Para versión corta reusable del criterio recursivo, usar `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`.

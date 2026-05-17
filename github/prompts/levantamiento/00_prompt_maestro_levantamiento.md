# GUÍA MAESTRA — LEVANTAMIENTO DE INFORMACIÓN

> Nota: este archivo es una guía de contexto general para todo el levantamiento. No se ejecuta como paso numerado.

## ÍNDICE DE PASOS EJECUTABLES
| Paso | Archivo | Objetivo | Salida esperada en `LEVANTAMIENTO_INFORMACION.md` |
|---|---|---|---|
| 1 | [01_vision_general.md](./01_vision_general.md) | Identificar la visión general y los servicios principales del orquestador. | Sección 1.0 |
| 2 | [02_contratos_wsdl_xsd.md](./02_contratos_wsdl_xsd.md) | Inventariar los contratos WSDL/XSD del orquestador con operaciones, campos y restricciones. | Secciones 2.0–3.0 |
| 3 | [03_analisis_esql_flujos_validaciones.md](./03_analisis_esql_flujos_validaciones.md) | Analizar los flujos, validaciones y la orquestación ESQL del orquestador. | Secciones 4.1–4.6 |
| 4 | [04_mapeo_apis_servicios.md](./04_mapeo_apis_servicios.md) | Mapear los servicios invocados por el orquestador con entrada, salida y manejo de respuesta. | Sección 4.7 |
| 5 | [05_mapeo_entrada_salida_errores.md](./05_mapeo_entrada_salida_errores.md) | Consolidar el mapeo de entrada/salida y el catálogo de errores del orquestador. | Secciones 4.8–4.10 |
| 6 | [06_resumen_servicios_dtos.md](./06_resumen_servicios_dtos.md) | Generar el resumen global de servicios, matrices consolidadas de campos entrada/salida por servicio, DTOs, clases candidatas y JSON request del orquestador. | Secciones 12.0–15.0 |

Actúa como un Arquitecto Senior Java especializado en levantamiento técnico de sistemas legacy 
(IBM Integration Bus / ESQL / SOAP / WSDL / WebSphere Application Server / EJB) para documentación y entendimiento funcional/técnico.

## AGENTE REQUERIDO
- `agente_analista_legacy`.

## OBJETIVO
Realizar un levantamiento técnico de los servicios que invoca el código fuente del proyecto,
identificando contratos, integraciones y dependencias de cada servicio, SIN modificar nada del sistema.

Regla de alcance obligatoria para análisis:
- El foco del levantamiento es identificar e inventariar todos los servicios invocados desde el código fuente (SOAP, REST, MQ, etc.).
- Las librerías externas no son requeridas para este análisis.

## ALCANCE DE ANÁLISIS (OBLIGATORIO)
Analiza TODO el árbol del proyecto incluyendo:
- Flujos: *.msgflow, *.subflow
- Lógica: *.esql
- Contratos: *.wsdl, *.xsd
- Estructura de carpetas y namespaces

## INSTRUCCIONES CLAVE
- NO omitas archivos.
- NO inventes información.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`.
- En toda referencia de evidencia ESQL, incluir número de línea y el texto exacto de la línea como comentario breve.
- Formato sugerido: `L185 // CALL InvocarCatalogoSubSectorEconomicoBancs() INTO respuesta;`
- Para secciones de DTOs y clases candidatas (paso 6), documentar de forma recursiva completa todas las clases internas/anidadas sin omitir niveles.
- Para secciones de DTOs y clases candidatas (paso 6), aplicar además `.github/prompts/shared/PROMPT_BASE_DTO_RECORDS.md`.
- Para versión corta reusable de la regla recursiva, usar `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`.
- Para secciones 13.3, 14 y 15 (paso 6), la cobertura debe ser 1:1 por servicio de la sección 12 (sin excepciones).
- Prohibido agrupar servicios en bloques tipo `RESTO`, `OTROS`, `PENDIENTES` o similares.
- Si falta evidencia de un servicio, mantener su bloque individual y registrar `FALTA DETALLE` + `Pista` dentro de ese mismo servicio.
- **Fuente de código**: analizar los archivos `.esql` ubicados en `repos/origen/proyecto-legado` para identificar los nombres de los servicios que invoca el orquestador.
- **Fuente de dependencias**: en `repos/origen/servicio-dependencia` se encuentran los archivos con la información de la URL del servicio y el request que se arma para invocarlo.
- A partir de ambas fuentes, documentar por cada servicio integrado: nombre del servicio (extraído del ESQL), URL/endpoint (extraído de servicio-dependencia) y estructura del request (extraída de servicio-dependencia).

## SALIDA OBLIGATORIA

Genera en la carpeta `./migracion/` (raíz del workspace), un único documento Markdown llamado:

LEVANTAMIENTO_INFORMACION.md

NO incluyas explicaciones fuera del documento.

## CONTENIDO DEL DOCUMENTO

## 1. VISIÓN GENERAL
- Nombre del proyecto
- Tipo de solución global: ORQUESTADOR (SIEMPRE)
- Componentes principales: analizar los archivos `.esql` en `repos/origen/proyecto-legado` para identificar todos los servicios que utiliza el orquestador. Cada servicio identificado es un componente principal y debe listarse aquí.

---

## 2. INVENTARIO DE SERVICIOS (WSDL)
Por cada WSDL:
- Si la entrada o salida de una operación es de tipo complejo, incluir también los complexType asociados (directos y anidados relevantes) con su estructura de campos.
- Extraer y documentar restricciones de campos definidas en XSD/WSDL (por ejemplo: `minOccurs`, `maxOccurs`, `length`, `minLength`, `maxLength`, `pattern`, `enumeration`, `nillable`, `default`, `fixed`, `minInclusive`, `maxInclusive`).
- Estas restricciones son insumo obligatorio para definir campos y validaciones de modelos en implementación (pasos 5 y 8).
- En las tablas de `Entrada` y `Salida`, documentar de forma recursiva todos los niveles de campos (hijos/nietos), incluyendo los que provienen de `complexType` y XSD externos/importados.
- Para cada fila, incluir ruta completa del campo, nivel jerárquico, ocurrencias (`minOccurs`/`maxOccurs`) y origen del esquema/tipo.

### Servicio: <nombre>
- Endpoint (si aplica)
- Operaciones

#### Operación: <nombre>
##### Entrada
| Nivel | Campo (ruta completa) | Tipo | Obligatorio | minOccurs | maxOccurs | Origen (XSD/ComplexType) | Restricciones XSD/WSDL |
|------|------------------------|------|------------|-----------|-----------|---------------------------|--------------------------|

##### Salida
| Nivel | Campo (ruta completa) | Tipo | Obligatorio | minOccurs | maxOccurs | Origen (XSD/ComplexType) | Restricciones XSD/WSDL |
|------|------------------------|------|------------|-----------|-----------|---------------------------|--------------------------|

##### ComplexType asociados (si aplica)
| ComplexType | Campo | Tipo | Obligatorio | minOccurs | maxOccurs | Restricciones XSD/WSDL |
|------------|-------|------|------------|-----------|-----------|--------------------------|

---

## 3. INVENTARIO DE XSD
- Estructuras principales (solo nombres)
- Tipos complejos (solo nombres; el detalle ya fue asociado en el paso 2)
- Reutilización de esquemas
- Reglas de validación por campo (facets/restricciones) que deben trasladarse a modelos objetivo.

---


## 4. ANÁLISIS DETALLADO ESQL (CRÍTICO)

Por cada archivo .esql:

### Archivo: <nombre.esql>

#### 4.1 Flujos del servicio (OBLIGATORIO — documentar primero)
Identificar todos los flujos que maneja cada archivo ESQL.

Patrón de detección obligatorio:
- Referencia al flujo: `DECLARE flujoIn REFERENCE TO Environment.entrada.bodyIn.flujo;`
- Identificación de flujos: buscar condicionales tipo `IF flujoIn.estado = '<nombre_flujo>' THEN` (ejemplo: `IF flujoIn.estado = 'iniciar' THEN`, `IF flujoIn.estado = 'consultar' THEN`, etc.)
- Cada valor distinto de `flujoIn.estado` encontrado en los `IF` representa un flujo que maneja el servicio.

| Flujo (valor de flujoIn.estado) | Línea/evidencia (Línea + comentario con texto ESQL) | Descripción funcional inferida |
|---------------------------------|-----------------|-------------------------------|

#### 4.2 Validaciones de campos (OBLIGATORIO)
Detectar todas las validaciones de campos definidas en el ESQL, típicamente dentro de procedimientos tipo `CREATE PROCEDURE ValidarEntrada() RETURNS BOOLEAN`.

Patrón de detección obligatorio:
- Validación de campo requerido: `IF LENGTH(TRIM(COALESCE(bodyIn.<ruta.campo>,''))) <= 0 THEN`
- Validación de campo numérico requerido: `IF LENGTH(TRIM(COALESCE(bodyIn.<ruta.campo>,'0'))) <= 0 THEN`
- Error generado: `SET Environment.salida.error.codigo = '<código>';` + `SET Environment.salida.error.mensaje = '<mensaje>';` + `SET Environment.salida.error.tipo = 'ERROR';`
- Validaciones con lógica adicional (ejemplo: validar que solo exista una dirección de correspondencia, transformaciones de valores como 'SI'→'S', 'NO'→'N', etc.)
- Llamadas a procedimientos auxiliares dentro de la validación (ejemplo: `CALL ConsultarCliente(...) INTO respuesta;`)

Documentar TODAS las validaciones encontradas en la siguiente tabla:

| Campo (ruta completa) | Tipo | Validación | Código error | Mensaje de error | Acción adicional (si aplica) | Línea/evidencia (Línea + comentario con texto ESQL) |
|------------------------|------|------------|--------------|------------------|------------------------------|-----------------|

Ejemplo de llenado:
| `bodyIn.identificacion.identificacion` | STRING | Campo requerido (no vacío) | 1 | Se requiere la identificación del cliente | — | L45 |
| `bodyIn.identificacion.tipoIdentificacion` | STRING | Campo requerido + consulta cliente | 2 | Se requiere el tipoIdentificacion del cliente | Llama a `ConsultarCliente()` si pasa validación | L50 |
| `bodyIn.localizacion.listaDirecciones.direccion[].correspondencia` | STRING | Solo una dirección con correspondencia='SI' | 10 | La dirección donde recibe la correspondencia debe ser solo una | — | L120 |

Reglas:
- Incluir validaciones de campos requeridos, transformaciones de valores y validaciones de reglas de negocio.
- Si un campo tiene valor por defecto (ejemplo: `COALESCE(bodyIn.campo,'0')` que setea a 0 en vez de error), documentarlo como "Valor por defecto: 0" en la columna de acción adicional.
- Si la validación invoca un procedimiento auxiliar, indicar el nombre del procedimiento.

#### 4.3 Flujo principal — Main (OBLIGATORIO)
Identificar los mensajes de éxito (OK) o error que se producen en la función `Main()` y la condición bajo la cual se generan.

Patrón de detección obligatorio:
- Estructura: `CREATE FUNCTION Main() RETURNS BOOLEAN`
- Llamada a validación: `CALL ValidarEntrada() INTO respuesta;` → si falla (`IF NOT respuesta THEN RETURN FALSE`), el error ya fue seteado en `ValidarEntrada()`.
- Llamada a orquestación: `CALL OrquestarWS() INTO respuesta;` → si falla (`IF NOT respuesta THEN RETURN FALSE`), el error fue seteado en `OrquestarWS()`.
- Respuesta exitosa: `SET Environment.salida.error.codigo = '0';` + `SET Environment.salida.error.mensaje = 'OK';` → se produce cuando tanto `ValidarEntrada()` como `OrquestarWS()` retornan `TRUE`.

Documentar el flujo de decisión y los mensajes resultantes:

| Condición | Procedimiento origen | Código respuesta | Mensaje respuesta | Resultado (RETURN) |
|-----------|---------------------|------------------|-------------------|--------------------|
| `ValidarEntrada()` retorna FALSE | ValidarEntrada | (definido en ValidarEntrada) | (definido en ValidarEntrada) | FALSE |
| `OrquestarWS()` retorna FALSE | OrquestarWS | (definido en OrquestarWS) | (definido en OrquestarWS) | FALSE |
| Ambos retornan TRUE | Main | 0 | OK | TRUE |

#### 4.4 Orquestación de servicios — Errores y secuencia (OBLIGATORIO)
Analizar la función de orquestación (típicamente `CREATE PROCEDURE OrquestarWS() RETURNS BOOLEAN`) para documentar:
1. La orquestación de servicios se realiza **por estado del flujo** (`IF flujoIn.estado = '<valor>' THEN`). Cada bloque de estado contiene su propia secuencia de servicios.
2. La secuencia de servicios invocados y el orden de ejecución dentro de cada flujo.
3. Los mensajes de error que se producen cuando un servicio falla.
4. Las condiciones especiales bajo las cuales se invoca o se omite un servicio.
5. Reintentos (si un servicio falla y se vuelve a invocar).

Regla clave: documentar la secuencia de servicios **agrupada por cada estado de flujo** detectado (ejemplo: `iniciar`, `consultar`, `actualizar`, etc.). Si un estado de flujo tiene una secuencia diferente de servicios, documentarla por separado.

Patrón de detección obligatorio:
- Agrupación por flujo: `IF flujoIn.estado = '<estado>' THEN ... END IF;`
- Invocación de servicio: `CALL <NombreProcedimiento>() INTO respuesta;`
- Fallo del servicio: `IF NOT respuesta THEN RETURN FALSE;` → el error fue seteado dentro del procedimiento invocado.
- Error directo en orquestación: `SET Environment.salida.error.codigo = '<código>';` + `SET Environment.salida.error.mensaje = '<mensaje>';`
- Condiciones de ejecución: `IF EXISTS(bodyIn.<ruta>[]) THEN`, `IF TRIM(COALESCE(bodyIn.<campo>,'')) IN ('valor1','valor2') THEN`, `IF COALESCE(bodyIn.<campo>,false) THEN`, etc.
- Reintentos: cuando se repite la misma llamada `CALL` dentro del bloque de fallo (ejemplo: si falla `InvocarSegmentacion()`, se vuelve a llamar antes de retornar FALSE).

Tabla de secuencia de orquestación:

| # Orden | Servicio invocado (procedimiento) | Flujo (estado) | Condición de ejecución | Reintento (SI/NO) | Error si falla (origen) | Línea/evidencia (Línea + comentario con texto ESQL) |
|---------|-----------------------------------|----------------|------------------------|--------------------|-------------------------|-----------------|

Ejemplo de llenado:
| 1 | InvocarCatalogoSubSectorEconomicoBancs() | iniciar | Ninguna | NO | Definido en el procedimiento | L10 |
| 2 | InvocarSegmentacion() | iniciar | Ninguna | SI (1 reintento) | Definido en el procedimiento | L15 |
| 3 | InvocarCreacionClienteProspecto() | iniciar | Ninguna | NO | Definido en el procedimiento | L25 |
| 3.1 | InvocarCrearLogModificaciones() | iniciar | Solo si paso 3 es exitoso | NO | Definido en el procedimiento | L27 |
| 10 | InvocarActualizacionRelacion() | iniciar | `EXISTS(bodyIn.complementaria.relaciones.relacion[])` AND `tipoInterviniente IN ('02','04','06')` | NO | Definido en el procedimiento | L80 |

Tabla de errores directos en orquestación (seteados dentro de `OrquestarWS`):

| Condición | Código error | Mensaje de error | Línea/evidencia (Línea + comentario con texto ESQL) |
|-----------|--------------|------------------|-----------------|

Ejemplo:
| `flujoIn.estado` no coincide con ningún flujo parametrizado (ELSE final) | 7 | Estado de flujo no parametrizado | L95 |

#### 4.5 Lógica de negocio por estado de flujo (OBLIGATORIO)
Documentar la lógica de negocio que se ejecuta para cada estado de flujo antes y durante la invocación de servicios. Presentar de forma clara y práctica qué hace cada flujo, qué condiciones evalúa, qué servicios llama y en qué orden.

Por cada estado de flujo detectado, generar una tabla resumen:

##### Flujo: `<valor de flujoIn.estado>`

| # Paso | Acción | Condición previa | Servicio invocado | Si falla | Si éxito |
|--------|--------|------------------|-------------------|----------|----------|

Ejemplo de llenado para flujo `iniciar`:

| # Paso | Acción | Condición previa | Servicio invocado | Si falla | Si éxito |
|--------|--------|------------------|-------------------|----------|----------|
| 1 | Consultar catálogo subsector económico | Ninguna | InvocarCatalogoSubSectorEconomicoBancs() | Retorna FALSE (error del servicio) | Continúa paso 2 |
| 2 | Segmentación del cliente | Ninguna | InvocarSegmentacion() | Reintenta 1 vez; si falla de nuevo retorna FALSE | Continúa paso 3 |
| 3 | Crear prospecto del cliente | Ninguna | InvocarCreacionClienteProspecto() | Retorna FALSE | Continúa paso 3.1 |
| 3.1 | Registrar log de modificaciones | Paso 3 exitoso | InvocarCrearLogModificaciones() | Retorna FALSE | Continúa paso 4 |
| 8 | Actualizar referencias | `EXISTS(bodyIn.referencias.listaReferencias.referencia[])` | InvocarActualizacionReferencias() | Retorna FALSE | Continúa paso 9 |
| 10 | Actualizar relaciones | `EXISTS(bodyIn.complementaria.relaciones.relacion[])` AND `tipoInterviniente IN ('02','04','06')` | InvocarActualizacionRelacion() | Retorna FALSE | Continúa paso 11 |
| 11 | Transmitir cliente a SFI | `bodyIn.sfi.flagGuardarSFI = true` | InvocarTransmisionClienteActualizarSFI() | Retorna FALSE | Finaliza flujo |

#### 4.6 Pseudocódigo del flujo completo (OBLIGATORIO)
Generar un pseudocódigo legible que consolide las validaciones de entrada y la orquestación de servicios, representando de forma simplificada todo el flujo del ESQL.

Formato de salida esperado:

```
FUNCIÓN Main():
  SI ValidarEntrada() falla:
    RETORNAR error (definido en ValidarEntrada)

  SI OrquestarWS() falla:
    RETORNAR error (definido en OrquestarWS)

  RETORNAR código=0, mensaje="OK"

PROCEDIMIENTO ValidarEntrada():
  SI campo_1 está vacío → error código=1, mensaje="..."
  SI campo_2 está vacío → error código=2, mensaje="..."
    SI NO → Llamar ConsultarCliente(campo_1, campo_2)
      SI falla → RETORNAR FALSE
  SI campo_N está vacío → error código=N, mensaje="..."
  ...
  RETORNAR TRUE

PROCEDIMIENTO OrquestarWS():
  SI flujo.estado = "iniciar":
    1. Llamar InvocarCatalogoSubSectorEconomicoBancs()
       SI falla → RETORNAR FALSE
    2. Llamar InvocarSegmentacion()
       SI falla → reintentar 1 vez
         SI falla de nuevo → RETORNAR FALSE
    3. Llamar InvocarCreacionClienteProspecto()
       SI falla → RETORNAR FALSE
       SI éxito → Llamar InvocarCrearLogModificaciones()
         SI falla → RETORNAR FALSE
    ...
    N. [último servicio]
       SI falla → RETORNAR FALSE

  SI flujo.estado = "<otro_estado>":
    ...

  SI flujo.estado no reconocido:
    error código=7, mensaje="Estado de flujo no parametrizado"
    RETORNAR FALSE

  mensaje = "finalizar"
  RETORNAR TRUE
```

Reglas:
- El pseudocódigo debe ser fiel al ESQL pero simplificado para lectura humana.
- Incluir las condiciones especiales (IF EXISTS, IF campo IN, IF flag=true, etc.).
- Documentar reintentos y dependencias entre pasos.
- No omitir ningún servicio ni validación.

#### 4.7 Mapeo de APIs invocadas — Entrada y valores fijos (OBLIGATORIO)
Por cada API/servicio invocado desde el ESQL (cada procedimiento tipo `InvocarXxx()`), identificar:
1. El nombre del servicio/API invocado.
2. La URL o endpoint del servicio (buscar en `repos/origen/servicio-dependencia`).
3. El mapeo de campos de entrada: qué dato del `bodyIn` o `Environment` se asigna a cada campo del request del servicio.
4. Los valores fijos (hardcodes) que se asignan directamente en el request sin provenir del `bodyIn`.

Patrón de detección obligatorio:
- Procedimiento de invocación: `CREATE PROCEDURE Invocar<NombreServicio>() RETURNS BOOLEAN`
- Mapeo de campos: `SET requestField = bodyIn.<ruta.campo>;` o `SET requestField = Environment.<ruta>;`
- Valores fijos: `SET requestField = '<valor_fijo>';` o `SET requestField = <número>;`
- URL del servicio: buscar en los archivos de `repos/origen/servicio-dependencia` la configuración del endpoint asociado.

Por cada API/servicio, documentar en las siguientes tablas:

##### API: `<NombreProcedimiento>` → Servicio: `<nombre_servicio>`
- URL/Endpoint: (extraído de servicio-dependencia)
- Método: (POST/GET/PUT, si aplica)

Tabla de mapeo de entrada:

| Campo request (servicio) | Origen (campo bodyIn / Environment) | Valor fijo (si aplica) | Tipo | Path XML SOAP origen | Path XML SOAP destino | Línea/evidencia (Línea + comentario con texto ESQL) |
|--------------------------|--------------------------------------|------------------------|------|----------------------|-----------------------|-----------------|

Ejemplo de llenado:
| `identificacion` | `bodyIn.identificacion.identificacion` | — | STRING | `//bodyIn/identificacion/identificacion` | `//soapenv:Body/ser:request/identificacion` | L25 |
| `tipoIdentificacion` | `bodyIn.identificacion.tipoIdentificacion` | — | STRING | `//bodyIn/identificacion/tipoIdentificacion` | `//soapenv:Body/ser:request/tipoIdentificacion` | L26 |
| `canal` | — | `"02"` | STRING | — (valor fijo) | `//soapenv:Body/ser:request/canal` | L27 |
| `institucion` | — | `"003"` | STRING | — (valor fijo) | `//soapenv:Body/ser:request/institucion` | L28 |
| `operacion` | `bodyIn.extra.operacion` | — | STRING | `//bodyIn/extra/operacion` | `//soapenv:Body/ser:request/operacion` | L30 |

Tabla de mapeo de salida (respuesta del servicio):

| Campo respuesta (servicio) | Destino (campo Environment / salida) | Tipo | Path XML SOAP origen (response) | Path XML SOAP destino (Environment) | Línea/evidencia (Línea + comentario con texto ESQL) |
|----------------------------|---------------------------------------|------|---------------------------------|--------------------------------------|-----------------|

Manejo de respuesta del servicio (OBLIGATORIO):
Identificar cómo se valida la respuesta de cada servicio invocado. El patrón típico es:
- Éxito: `IF service.error.codigo = '0' THEN RETURN TRUE;`
- Error: `ELSE SET Environment.salida.error = service.error; RETURN FALSE; END IF;`

Si el servicio NO mapea campos de salida al éxito (solo retorna TRUE) y en caso de error propaga el error del servicio a `Environment.salida.error`, documentarlo así:

| Resultado | Condición | Acción | Línea/evidencia (Línea + comentario con texto ESQL) |
|-----------|-----------|--------|-----------------|
| Éxito | `service.error.codigo = '0'` | Retorna TRUE (sin mapeo de salida) | L__ |
| Error | `service.error.codigo != '0'` | Propaga error: `Environment.salida.error = service.error` → Retorna FALSE | L__ |

Reglas:
- Documentar TODAS las APIs/servicios detectados, sin omitir ninguno.
- Si un campo del request tiene un valor fijo, indicarlo claramente en la columna "Valor fijo".
- Si el origen es una transformación (ejemplo: `TRIM(COALESCE(bodyIn.campo,''))`) documentar la transformación en la columna "Origen".
- Los XML SOAP de origen y destino se encuentran ambos en `repos/origen/servicio-dependencia`, identificados por el nombre del archivo:
  - **Path XML SOAP origen**: corresponde al XML del servicio orquestador (el archivo con el nombre del orquestador). Es la ruta XPath del campo dentro del request SOAP del orquestador.
  - **Path XML SOAP destino**: corresponde al XML del servicio que se invoca (el archivo con el nombre del servicio invocado). Es la ruta XPath del campo dentro del request SOAP del servicio destino.
- Si no se encuentra el XML de ejemplo en servicio-dependencia, marcar `FALTA DETALLE` con la pista del procedimiento.

#### 4.8 Mapeo de entrada del orquestador (OBLIGATORIO)
Documentar todos los campos que recibe el orquestador como entrada (request del orquestador), identificando la ruta completa del campo, el tipo de dato y su path en el XML SOAP del orquestador (obtenido de `repos/origen/servicio-dependencia`, archivo con el nombre del orquestador).

| Campo (ruta bodyIn) | Tipo | Path XML SOAP (request orquestador) | Descripción / Uso |
|----------------------|------|--------------------------------------|-------------------|

#### 4.9 Mapeo de salida del orquestador (OBLIGATORIO)
Documentar todos los campos que devuelve el orquestador como salida (response del orquestador), identificando la ruta completa del campo en `Environment.salida`, el tipo de dato y su path en el XML SOAP de respuesta del orquestador.

| Campo (ruta Environment.salida) | Tipo | Path XML SOAP (response orquestador) | Origen del dato (servicio / validación / fijo) |
|----------------------------------|------|---------------------------------------|------------------------------------------------|

#### 4.10 Consolidado de mensajes de error (OBLIGATORIO)
Tabla consolidada de TODOS los mensajes de error detectados en el ESQL, agrupando los provenientes de:
- Validaciones de entrada (`ValidarEntrada`)
- Orquestación de servicios (`OrquestarWS`)
- Respuestas de servicios invocados (propagación de `service.error`)
- Cualquier otro `SET Environment.salida.error.*` encontrado en el ESQL

| # | Código error | Mensaje de error | Tipo | Origen (procedimiento/función) | Condición que lo genera | Línea/evidencia (Línea + comentario con texto ESQL) |
|---|--------------|------------------|------|-------------------------------|-------------------------|-----------------|

Reglas:
- Incluir errores explícitos (`SET Environment.salida.error.codigo = '<código>'`) y errores propagados desde servicios (`SET Environment.salida.error = service.error`).
- Para errores propagados desde servicios, indicar en la columna "Origen" el nombre del procedimiento que invoca el servicio y en "Condición" que el error proviene de la respuesta del servicio (`service.error.codigo != '0'`).
- No omitir ningún error. Si se detectan bloques TRY/CATCH con manejo de excepciones, documentarlos también.
- Ordenar la tabla por código de error de menor a mayor.



````markdown
# IMPLEMENTACIÓN 01 — GENERACIÓN DE DTOs DESDE LEVANTAMIENTO

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 2, 4.7, 4.8, 4.9, 12, 13 (incluyendo matrices 13.1 y 13.2) y 14 disponibles y completas.

## OBJETIVO
Generar modelos DTO Java de entrada y salida para el orquestador y sus servicios invocados, con base en el levantamiento:
1. Del **orquestador** (basado en los campos del WSDL/XSD del orquestador documentados en el levantamiento).
2. De los **servicios invocados** (basado en las secciones 4.7, 12, 13 y 14 del levantamiento).
3. Generar DTOs de **request y response completos**, incluyendo todos los campos documentados en `LEVANTAMIENTO_INFORMACION.md`.

Regla crítica:
- Este paso NO está completo si solo se generan DTOs de servicios invocados.
- Es obligatorio generar primero los DTOs del webservice del orquestador (request/response + anidados), y luego los de dependencias.

## ALCANCE
- Crear records DTO en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`
- Generar DTOs de request y response para orquestador y cada servicio invocado.
- Incluir todos los campos, subestructuras y colecciones reportadas en el levantamiento (sin omitir campos de response).
- Generar cobertura de estructuras anidadas y colecciones según contrato.
- Aplicar reglas técnicas de records según prompt compartido.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Campos del orquestador en secciones de contratos (WSDL/XSD y mapeo de entrada/salida) — secciones 2, 4.8 y 4.9.
2. Servicios invocados: usar como fuente principal las matrices consolidadas de la sección 13.1 (entrada) y 13.2 (salida).
3. Complementar con detalle por servicio en sección 13.3, mapeo 4.7 y JSON de ejemplo 14 cuando aplique.
4. Si hay inconsistencias, preferir lo **evidenciado** con trazabilidad y marcar `FALTA DETALLE` en comentario TODO del DTO.

Regla específica para DTOs del orquestador:
- Para el orquestador, la fuente primaria NO son las matrices 13.1/13.2 de servicios invocados.
- La fuente primaria del orquestador es:
  1. Sección 2 (WSDL/XSD del orquestador: operación, entrada, salida, complexTypes)
  2. Sección 4.8 (mapeo de entrada del orquestador)
  3. Sección 4.9 (mapeo de salida del orquestador)
- Si hay conflicto entre 2/4.8/4.9 para el orquestador, priorizar lo evidenciado en contratos (sección 2) y completar con 4.8/4.9.

Regla obligatoria de completitud:
- No omitir ningún campo documentado para DTOs request y response.
- Si una sección trae solo respuesta mínima (por ejemplo `error.codigo`) y hay evidencia adicional en otras secciones, consolidar y modelar el response completo con toda la evidencia disponible.
- En campos no verificables por falta de contrato externo, mantener el campo con tipo conservador y `TODO: verificar tipo en contrato dependiente`.
- Para servicios invocados, la estructura final de DTO debe salir 1:1 de las filas de las matrices 13.1/13.2 (más DTOs hijos requeridos para colecciones/anidación).

Convención de modelado desde matrices 13.1/13.2:
- Interpretar rutas de campo en formato `dot.path`.
- No interpretar `[]` en nombres de campo (si apareciera en evidencias antiguas, normalizar a ruta sin `[]`).
- La condición de colección se define por el tipo `List<...>`.
- Mantener sufijo `DTO` consistente en tipos anidados.

### 2) DTOs del orquestador
Generar en:
- `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`

Records mínimos esperados:
- `OrquestadorRequestDto`
- `OrquestadorResponseDto`
- Records hijos necesarios (header/body/error/colecciones).

Convención obligatoria para el orquestador:
- Debe existir DTO raíz de request y DTO raíz de response del orquestador.
- Deben existir DTOs hijos para cada objeto complejo usado por el request/response del orquestador.
- No dejar DTO raíz con componentes vacíos cuando hay evidencia de campos en sección 2/4.8/4.9.
- Si `headerIn/headerOut/error` dependen de XSD compartidos externos y no hay detalle completo, aún así:
  - generar los DTOs correspondientes,
  - conservar los campos evidenciados,
  - marcar faltantes con `TODO: verificar tipo en contrato dependiente`.

Cobertura mínima esperada del orquestador:
- Request: `headerIn` + `bodyIn` con desglose recursivo completo.
- Response: `headerOut` + `bodyOut` + `error` con desglose recursivo completo.

Reglas:
- Mantener correspondencia de estructura con el request/response SOAP del orquestador.
- Para arreglos/colecciones: modelar estructura equivalente al contrato y crear DTOs anidados requeridos.

### 3) DTOs de servicios invocados
Generar en:
- `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`

Por cada servicio invocado del resumen global (sección 12), crear:
- `<Servicio><Operacion>RequestDto`
- `<Servicio><Operacion>ResponseDto`

Obligatorio por servicio:
- El `RequestDto` debe incluir todos los campos documentados para entrada.
- El `ResponseDto` debe incluir todos los campos documentados para salida (no limitarse a `error.codigo`).
- Incluir DTOs hijos/anidados de request y response cuando existan estructuras complejas o colecciones.

Si no se conoce la operación exacta, usar:
- `<Servicio>RequestDto`
- `<Servicio>ResponseDto`

Regla de alcance:
- Los DTOs de servicios invocados NO reemplazan ni absorben los DTOs del orquestador.
- Mantener separación explícita entre:
  - DTOs del contrato del orquestador
  - DTOs de contratos de servicios invocados

Ejemplos:
- `WsClientes0096CrearProspecto21RequestDto`
- `WsClientes0096CrearProspecto21ResponseDto`

### 4) Tipos Java
Inferir tipos con estas reglas:
- `xs:string` -> `String`
- `xs:int` / `xs:integer` -> `Integer`
- `xs:long` -> `Long`
- `xs:decimal` -> `BigDecimal`
- `xs:boolean` -> `Boolean`
- `xs:date` -> `LocalDate`
- `xs:dateTime` -> `LocalDateTime`

Si el tipo no está confirmado:
- usar `String`
- agregar comentario: `// TODO: verificar tipo en contrato dependiente`

### 5) Reglas de generación de records
Aplicar exactamente convenciones de DTO `record` del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_DTO_RECORDS.md`
- Para versión corta reutilizable en nuevos prompts, usar también:
  - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`

### 6) Estructura de carpetas y paquetes
Regla obligatoria:
- NO crear subcarpetas `orquestor` ni `ws`.
- Todos los DTOs (orquestador y servicios invocados) deben quedar en la misma carpeta `dto`.

## SALIDA OBLIGATORIA
- Records DTO Java creados en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`

## CHECKLIST DE VALIDACIÓN
- [ ] DTO request del orquestador generado
- [ ] DTO response del orquestador generado
- [ ] DTOs hijos del orquestador (header/body/error/anidados) generados con estructura no vacía cuando existe evidencia
- [ ] `bodyIn` y `bodyOut` del orquestador modelados de forma recursiva desde sección 2
- [ ] DTOs request/response para cada servicio invocado del resumen global
- [ ] Cada DTO request contiene todos los campos documentados en el levantamiento
- [ ] Cada DTO response contiene todos los campos documentados en el levantamiento
- [ ] Cobertura 1:1 validada entre DTOs generados y matrices 13.1/13.2
- [ ] Separación validada: DTOs del orquestador no mezclados con DTOs de servicios invocados
- [ ] Convenciones de `record` validadas según prompt base compartido
- [ ] Estructuras anidadas y colecciones modeladas según contrato
- [ ] Tipos Java inferidos desde WSDL/XSD/levantamiento
- [ ] Campos ambiguos marcados con `TODO: verificar tipo`
- [ ] Paquetes y rutas correctas bajo `repos/destino/${nombre_proyecto}`
- [ ] Compilación sin errores de imports en records DTO generados

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar campos fuera de lo documentado en el levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_DTO_RECORDS.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`

````

````markdown
# IMPLEMENTACIÓN 01 — GENERACIÓN DE DTOs DESDE LEVANTAMIENTO

> Nota: este archivo corresponde a un paso ejecutable de implementación.

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 2, 4.7, 4.8, 4.9, 12, 13 y 14 disponibles.

## OBJETIVO
Generar clases DTO Java de entrada y salida:
1. Del **orquestador** (basado en los campos del WSDL/XSD del orquestador documentados en el levantamiento).
2. De los **servicios invocados** (basado en las secciones 4.7, 12, 13 y 14 del levantamiento).

## ALCANCE
- Crear clases DTO en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`
- Generar DTOs de request y response.
- Crear clases anidadas para colecciones cuando aplique (`List<...>`).

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Campos del orquestador en secciones de contratos (WSDL/XSD y mapeo de entrada/salida).
2. Servicios invocados en secciones 4.7, 12, 13 y 14.
3. Si hay inconsistencias, preferir lo **evidenciado** con trazabilidad y marcar `FALTA DETALLE` en comentario TODO del DTO.

### 2) DTOs del orquestador
Generar en:
- `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`

Clases mínimas esperadas:
- `OrquestadorRequestDto`
- `OrquestadorResponseDto`
- Clases hijas necesarias (header/body/error/colecciones).

Reglas:
- Nombrado obligatorio en `PascalCase` con postfijo `Dto`.
- Campos en `camelCase`.
- Mantener correspondencia de estructura con el request/response SOAP del orquestador.
- Para arreglos/colecciones: usar `List<NombreItemDto>` y crear su clase.

### 3) DTOs de servicios invocados
Generar en:
- `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`

Por cada servicio invocado del resumen global (sección 12), crear:
- `<Servicio><Operacion>RequestDto`
- `<Servicio><Operacion>ResponseDto`

Si no se conoce la operación exacta, usar:
- `<Servicio>RequestDto`
- `<Servicio>ResponseDto`

Ejemplos:
- `WsClientes0096CrearProspecto21RequestDto`
- `WsClientes0096CrearProspecto21ResponseDto`

Regla de nombrado obligatoria:
- TODAS las clases DTO deben terminar en `Dto`.
- No se permiten clases DTO sin ese postfijo.

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

### 5) Reglas de generación de clases
- No incluir lógica de negocio.
- No incluir validaciones en métodos.
- No generar constructores manuales salvo necesidad explícita.
- Mantener clases simples para transporte de datos.
- Crear imports mínimos necesarios.

### 6) Estructura de carpetas y paquetes
- Package único para todos los DTOs:
  - `com.pichincha.sp.domain.dto`

Regla obligatoria:
- NO crear subcarpetas `orquestor` ni `ws`.
- Todos los DTOs (orquestador y servicios invocados) deben quedar en la misma carpeta `dto`.

## SALIDA OBLIGATORIA
- Clases DTO Java creadas en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/domain/dto`

## CHECKLIST DE VALIDACIÓN
- [ ] DTO request del orquestador generado
- [ ] DTO response del orquestador generado
- [ ] DTOs request/response para cada servicio invocado del resumen global
- [ ] Todos los nombres de clases DTO terminan en `Dto`
- [ ] Colecciones modeladas con `List<...>` y clases anidadas
- [ ] Tipos Java inferidos desde WSDL/XSD/levantamiento
- [ ] Campos ambiguos marcados con `TODO: verificar tipo`
- [ ] Paquetes y rutas correctas bajo `repos/destino/${nombre_proyecto}`
- [ ] Compilación sin errores de imports en DTOs generados

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar campos fuera de lo documentado en el levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_DTO_LOMBOK_GETTER_SETTER.md`

````

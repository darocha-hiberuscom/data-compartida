````markdown
# IMPLEMENTACIÓN 04 — GENERACIÓN DE MODELOS REQUEST/RESPONSE EN ADAPTER OUTPUT

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs de dominio generados como `record`).
- Paso 03 completado (puertos de salida `PortOutput` generados).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 4.7, 12 y 13 disponibles para derivar invocaciones y contratos.

## OBJETIVO
Generar modelos `request` y `response` como `record` en la capa `infrastructure.output.adapter`, uno por cada servicio invocado por el orquestador.

Adicionalmente, estandarizar el manejo de errores SOAP para que se use únicamente estructura de error HTTP `500` en el servicio SOAP, con captura centralizada por `ErrorResolverHandler` + `ErrorResolver`.

## ALCANCE
- Crear records de request en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter/request`
- Crear records de response en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter/response`
- Generar cobertura 1:1 por invocación de webservice documentada.
- Aplicar reglas de naming/estructura definidas en prompt compartido.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.7 — Mapeo de APIs / servicios invocados (fuente principal de request/response por invocación).
2. Sección 12 — Resumen global de servicios invocados.
3. Sección 13 — DTOs de entrada/salida por servicio.
4. Sección 2 — Inventario WSDL/XSD (para contraste de tipos cuando aplique).

Si hay inconsistencias:
- preferir lo evidenciado con trazabilidad,
- y marcar `FALTA DETALLE` como TODO en el record correspondiente.

### 2) Cobertura por invocación
Por cada invocación a webservice identificada en la sección 4.7, generar:
- Un `record` request.
- Un `record` response.

Reglas obligatorias:
- Debe existir correspondencia 1:1 entre invocación y par request/response.
- Si una invocación no está evidenciada en 4.7, no generar el modelo y registrar `FALTA DETALLE` con pista trazable.

### 3) Convención y estructura técnica
Aplicar exactamente las reglas del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_OUTPUT_ADAPTER_RECORDS.md`
- Para regla corta reutilizable (recursividad completa), considerar además:
  - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER_MIN.md`

### 4) Compatibilidad con capa de aplicación
- Mantener alineación de campos con los DTOs de dominio del paso 01.
- No redefinir DTOs de dominio dentro de `application`.
- Estos records son exclusivos de `infrastructure.output.adapter`.

### 5) Manejo de errores SOAP (solo estructura 500)
Aplicar de forma obligatoria en el diseño/uso de modelos de error:

- Usar únicamente estructura SOAP de error con estado `500` (`SoapFaultEnvelopeDto` / `SoapFaultBodyDto` / `SoapFaultDto`).
- La captura y resolución de errores debe pasar por `ErrorResolverHandler` (no manejo distribuido por adapter/controller para errores SOAP).
- Mantener el patrón existente de resolvers en:
  - `infrastructure.exception.error.resolver.handler.ErrorResolverHandler`
  - `infrastructure.exception.error.resolver.ErrorResolver`
  - `GlobalErrorExceptionResolver`
  - `ResponseStatusExceptionResolver`
  - `UnexpectedErrorResolver`

Errores que se deben considerar manejables en el servicio SOAP (alineados al handler actual):
- `GlobalErrorException`
- `ResponseStatusException`
- `ConstraintViolationException` (por fallback del handler)
- `Throwable` no controlado (fallback a `UnexpectedErrorResolver`)

Reglas obligatorias:
- No generar estructuras de error SOAP alternativas para 4xx.
- No inventar nuevos envelopes de error fuera de `SoapFaultEnvelopeDto`.
- Si falta detalle de catálogo/código, registrar `FALTA DETALLE` con trazabilidad y mantener formato SOAP 500.

## SALIDA OBLIGATORIA
- Records request y response creados en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter/request`
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter/response`

## CHECKLIST DE VALIDACIÓN
- [ ] Se generó request/response por cada invocación de webservice documentada
- [ ] Todos los modelos fueron creados como `record`
- [ ] Convenciones de naming validadas según prompt base compartido
- [ ] Campos y tipos alineados con levantamiento (4.7, 12, 13)
- [ ] Campos ambiguos marcados con `TODO: verificar tipo` o `FALTA DETALLE`
- [ ] Estructura de error SOAP unificada en `500` (`SoapFaultEnvelopeDto`)
- [ ] Captura de errores SOAP definida vía `ErrorResolverHandler`
- [ ] Tipos de error manejables alineados a resolvers existentes del servicio SOAP
- [ ] Compilación sin errores de imports en modelos generados

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar campos fuera de lo documentado en el levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_OUTPUT_ADAPTER_RECORDS.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_SOAP_ERROR_500_RESOLVER_MIN.md`

````
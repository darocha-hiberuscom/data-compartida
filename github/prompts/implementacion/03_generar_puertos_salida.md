````markdown
# IMPLEMENTACIÓN 03 — GENERACIÓN DE PUERTOS DE SALIDA

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs generados como `record`).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Sección 4.7 disponible para derivar invocaciones de webservice.
- Secciones 12 y 13 disponibles para contexto de servicios y DTOs.

## OBJETIVO
Generar puertos de salida (interfaces) para cada invocación a webservice documentada en el levantamiento, usando DTOs del dominio.

## ALCANCE
- Crear interfaces de puertos de salida en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/port/output`
- Generar cobertura 1:1: un puerto por cada invocación a webservice identificada.
- Aplicar reglas de firma, naming y estructura definidas en prompt compartido.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.7 — Mapeo de APIs / servicios invocados: fuente principal de invocaciones `Invocar*()`.
2. Sección 12 — Resumen global de servicios invocados.
3. Sección 13 — DTOs de entrada/salida por servicio.
4. Sección 2 — Inventario de servicios (WSDL), para contraste cuando aplique.

Si hay inconsistencias:
- preferir lo evidenciado con trazabilidad,
- y marcar `FALTA DETALLE` en comentario TODO dentro de la interfaz.

### 2) Convención de puertos de salida
Por cada invocación a webservice identificada, crear una interfaz con formato:
- `<Servicio><Operacion>PortOutput`

Si no se puede identificar operación explícita, usar:
- `<Servicio>PortOutput`

Ejemplos:
- `WsClientes0096CrearProspecto21PortOutput`
- `WsClientes0055PortOutput`

Reglas obligatorias:
- Debe existir correspondencia 1:1 entre invocación de webservice y puerto de salida.
- Si una invocación no está evidenciada en la sección 4.7, no generar puerto y registrar `FALTA DETALLE` con pista trazable.

### 3) Firma de métodos
Aplicar exactamente la firma y convenciones del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_PORT_OUTPUT_REACTIVO.md`

### 4) Paquetes y estructura
Reglas obligatorias:
- No crear puertos en capa `infrastructure`.
- No mezclar puertos de entrada y salida en el mismo package.
- No crear subcarpetas innecesarias dentro de `port/output`.

### 5) Compatibilidad con DTOs
- Reusar los DTOs `record` ya generados.
- No redefinir DTOs dentro de `application.port.output`.
- Mapear DTOs en base a secciones 4.7, 12 y 13 del levantamiento.

## SALIDA OBLIGATORIA
- Interfaces Java de puertos de salida creadas en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/port/output`

## CHECKLIST DE VALIDACIÓN
- [ ] Se generó un puerto de salida por cada invocación a webservice documentada
- [ ] Invocaciones sin evidencia en sección 4.7 registradas como `FALTA DETALLE` con pista trazable
- [ ] No hay lógica de negocio en interfaces
- [ ] Convenciones de `PortOutput` validadas según prompt base compartido
- [ ] Compilación sin errores de imports en puertos generados

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar invocaciones fuera de lo documentado en el levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_PORT_OUTPUT_REACTIVO.md`

````
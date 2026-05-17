````markdown
# IMPLEMENTACIÓN 02 — GENERACIÓN DE PUERTOS DE ENTRADA

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs generados como `record`).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Sección 2 disponible para derivar operaciones del WSDL.
- Secciones 4.9, 12 y 13 disponibles para contexto de mapeo y DTOs.

## OBJETIVO
Generar puertos de entrada (interfaces) para las operaciones del orquestador documentadas en el levantamiento, usando los DTOs definidos en el paso 01.

## ALCANCE
- Crear interfaces de puertos de entrada en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/port/input`
- Generar cobertura 1:1: un puerto por cada operación WSDL del orquestador.
- Aplicar reglas de firma, naming y estructura definidas en prompt compartido.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 2 — INVENTARIO DE SERVICIOS (WSDL): fuente principal de operaciones WSDL.
2. Sección 4.9 (mapeo de entrada/salida del orquestador).
3. Sección 12 (resumen global de servicios invocados).
4. Sección 13 (DTOs de entrada/salida por servicio).

Si hay inconsistencias:
- preferir lo evidenciado con trazabilidad,
- y marcar `FALTA DETALLE` en comentario TODO dentro de la interfaz.

### 2) Convención de puertos de entrada
Por cada operación del WSDL identificada, crear una interfaz con formato:
- `<Operacion>PortInput`

Ejemplos:
- `CrearProspectoPortInput`
- `ConsultarClientePortInput`

Reglas obligatorias:
- El método principal debe representar una sola intención de negocio por puerto.
- Debe existir correspondencia 1:1 entre operación WSDL y puerto de entrada.
- Si una operación no está evidenciada en la sección 2, no generar puerto y registrar `FALTA DETALLE` con pista trazable.

### 3) Firma de métodos
Aplicar exactamente la firma y convenciones del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_PORT_INPUT_REACTIVO.md`

### 4) Paquetes y estructura
Reglas obligatorias:
- No crear puertos en capa `infrastructure`.
- No mezclar puertos de entrada y salida en el mismo package.
- No crear subcarpetas innecesarias dentro de `port/input`.

### 5) Compatibilidad con DTOs
- Reusar los DTOs `record` ya generados.
- No redefinir DTOs dentro de `application.port.input`.
- Mapear DTOs en base a secciones 4.9 y 13 del levantamiento.

## SALIDA OBLIGATORIA
- Interfaces Java de puertos de entrada creadas en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/port/input`

## CHECKLIST DE VALIDACIÓN
- [ ] Se generó un puerto de entrada por cada operación del WSDL del orquestador
- [ ] Operaciones sin evidencia en sección 2 registradas como `FALTA DETALLE` con pista trazable
- [ ] No hay lógica de negocio en interfaces
- [ ] Convenciones de `PortInput` validadas según prompt base compartido
- [ ] Compilación sin errores de imports en puertos generados

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar operaciones fuera de lo documentado en el levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_PORT_INPUT_REACTIVO.md`

````
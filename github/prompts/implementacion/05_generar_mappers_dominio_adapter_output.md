````markdown
# IMPLEMENTACIÓN 05 — GENERACIÓN DE MAPPERS DOMINIO ↔ ADAPTER OUTPUT

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 01 completado (DTOs de dominio `record`).
- Paso 03 completado (puertos de salida `PortOutput`).
- Paso 04 completado (records request/response de adapter output).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 4.7, 4.9, 12 y 13 disponibles.

## OBJETIVO
Generar mappers para transformar DTOs de dominio a modelos técnicos request del adapter, y modelos técnicos response del adapter a DTOs de dominio, usando MapStruct de forma obligatoria.

## ALCANCE
- Crear mappers en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter/mapper`
- Generar cobertura 1:1: un mapper por cada invocación a servicio (o por operación).
- Desacoplar services/adapters de conversiones manuales repetitivas.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.7 — Mapeos de entrada/salida por servicio invocado.
2. Sección 13 — DTOs request/response por servicio.
3. Sección 12 — Resumen global de servicios.
4. Sección 4.9 — Estructura de salida consolidada del orquestador.

Si hay inconsistencias:
- preferir evidencia trazable,
- registrar `FALTA DETALLE` con TODO en el mapper.

### 2) Cobertura por servicio/operación
Por cada servicio/operación invocado, crear un mapper con al menos:
- Método de entrada: dominio request -> adapter request.
- Método de salida: adapter response -> dominio response.

Reglas obligatorias:
- Debe existir correspondencia 1:1 entre invocación y mapper.
- Si no hay evidencia de mapeo suficiente, no inventar campos.
- Cumplir de forma obligatoria la política de mapeo MapStruct definida en el prompt compartido.

### 3) Convención y estructura técnica
Aplicar exactamente las reglas del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_MAPPERS_DOMINIO_ADAPTER.md`

### 4) Integración con services y adapters
Ajustar (si aplica) los services/adapters para reutilizar mappers generados.

Reglas:
- No alterar contratos `PortInput` y `PortOutput`.
- Mantener flujo reactivo `Mono<...>`.
- Evitar duplicación de mapeos inline en múltiples clases.

## SALIDA OBLIGATORIA
- Clases mapper creadas en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/output/adapter/mapper`
- Conversión dominio↔adapter cubierta para cada invocación documentada.

## CHECKLIST DE VALIDACIÓN
- [ ] Se generó un mapper por servicio/operación invocado
- [ ] Cada mapper incluye `toAdapterRequest(...)`
- [ ] Cada mapper incluye `toDomainResponse(...)`
- [ ] Se cumple la política de mapeo MapStruct del prompt compartido
- [ ] No hay lógica de negocio en mappers
- [ ] Se reusan records existentes (sin redefiniciones)
- [ ] Campos ambiguos marcados con `TODO: verificar tipo` o `FALTA DETALLE`
- [ ] Compilación sin errores de imports en mappers

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar campos no evidenciados en levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_POLITICA_MAPPERS_MAPSTRUCT.md`
  - `.github/prompts/shared/PROMPT_BASE_MAPPERS_DOMINIO_ADAPTER.md`

````
````markdown
# IMPLEMENTACIÓN 12 — VALIDACIONES DEL LEGADO BASADAS EN LEVANTAMIENTO

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 09 completado (services de orquestación implementando `PortInput`).
- Paso 10 completado (controller input y request mapper completados).
- Paso 11 completado (presenter/response mapper input completados).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Secciones 4.7, 4.8, 4.9, 4.10, 12 y 13 disponibles.

## OBJETIVO
Implementar las validaciones funcionales del proyecto legado en la solución migrada, basándose estrictamente en el levantamiento y manteniendo trazabilidad de códigos/mensajes de error.

## ALCANCE
- Implementar/ajustar validaciones en:
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/application/service`
  - `repos/destino/${nombre_proyecto}/src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/validator`
- Integrar validaciones en el flujo de orquestación y entrada SOAP sin romper contratos existentes.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.10 — Consolidado de errores (códigos/mensajes/reglas).
2. Sección 4.8 — Reglas de validación de entrada.
3. Sección 4.9 — Estructura de salida y propagación de error funcional.
4. Sección 4.7 — Condiciones de flujo y validaciones por invocación.

Si hay inconsistencias:
- preferir evidencia trazable,
- registrar `FALTA DETALLE` con TODO en el punto de validación.

### 2) Implementación de validaciones
Por cada validación funcional evidenciada en el legado:
- implementar condición de validación,
- asociar código y mensaje funcional correspondiente,
- integrar la validación en el punto correcto del flujo (entrada o orquestación).

Reglas obligatorias:
- No inventar validaciones no documentadas.
- Mantener compatibilidad con patrón `BusinessException` del proyecto.
- Preservar trazabilidad de `resource`, `applicationName` y `backend` cuando aplique.
- No hardcodear códigos/mensajes funcionales configurables en validator/service; leerlos desde `application.yml` (`@Value`/`@ConfigurationProperties`).
- Asegurar que esas variables estén externalizadas con `${...}` y presentes en Helm (`variables.own.config[].name`) por ambiente.
- Implementar validaciones con enfoque mantenible y declarativo (catálogo/lista de reglas, strategy/specification o equivalente).
- Evitar validadores con cadenas largas de `if` secuenciales para cada campo/regla.
- Centralizar la construcción de `BusinessException` para no duplicar código.

### 3) Integración con controller/service
- Reusar validadores existentes del input adapter cuando existan.
- Reusar services del paso 09 para reglas de orquestación.
- Reusar presenter del paso 11 para respuesta de errores funcionales.

Reglas:
- No duplicar validaciones en múltiples capas sin justificación.
- Diferenciar errores funcionales vs técnicos.

### 4) Estándar técnico compartido
Aplicar exactamente las reglas de:
- `.github/prompts/shared/PROMPT_BASE_VALIDACIONES_LEGADO.md`

## SALIDA OBLIGATORIA
- Validaciones funcionales del legado implementadas en la solución migrada.
- Códigos y mensajes de error funcional alineados con levantamiento.
- Flujo de error funcional integrado desde validator/service hasta respuesta SOAP.

## CHECKLIST DE VALIDACIÓN
- [ ] Todas las validaciones funcionales evidenciadas en 4.10 fueron implementadas
- [ ] Códigos/mensajes funcionales alineados con levantamiento
- [ ] Códigos/mensajes funcionales configurables leídos desde `application.yml` (sin literales)
- [ ] Variables de error funcional existen en Helm `dev/test/prod`
- [ ] El validador usa patrón mantenible (reglas declarativas/catálogo) y no cadena larga de `if`
- [ ] La construcción de `BusinessException` está centralizada/reutilizable
- [ ] Validaciones de entrada alineadas con sección 4.8
- [ ] Validaciones de orquestación alineadas con secciones 4.7 y 4.9
- [ ] Errores funcionales y técnicos diferenciados correctamente
- [ ] Campos/reglas ambiguas marcadas como `FALTA DETALLE`
- [ ] Compilación sin errores de imports en validadores/services ajustados

## REGLAS
- NO modificar lógica del negocio fuera del alcance de validaciones.
- NO inventar reglas no evidenciadas en el levantamiento.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_VALIDACIONES_LEGADO.md`

````
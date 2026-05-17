# CHECKLIST OPERATIVO POR INTENTOS — IMPLEMENTACIÓN 01–15

Uso: control de ejecución por intentos (Intento 1, 2, 3...) para cada paso.
Referencia principal: [CHECKLIST_OPERATIVO_01_15.md](CHECKLIST_OPERATIVO_01_15.md)
Bitácora oficial: `migracion/bitacora_intentos_pasos.csv`

## Reglas rápidas
- Ejecutar en orden 01 → 15.
- Un paso no avanza mientras el intento vigente esté en FAIL sin acción correctiva.
- Registrar cada intento en la bitácora CSV.
- Si hay falta de evidencia, registrar FALTA DETALLE + Pista.

---

## Plantilla por paso (copiar y repetir)

### Paso XX — <nombre del paso>

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado:
- Cambios esperados:
- Validación ejecutada:
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción:

#### Intento 2
- Fecha/Hora:
- Prompt ejecutado:
- Cambios esperados:
- Validación ejecutada:
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción:

#### Intento 3
- Fecha/Hora:
- Prompt ejecutado:
- Cambios esperados:
- Validación ejecutada:
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción:

---

## Registro operativo resumido por paso

| Paso | Último intento | Estado actual | ¿Bloquea avance? | Próxima acción |
|---|---:|---|---|---|
| 01 |  |  |  |  |
| 02 |  |  |  |  |
| 03 |  |  |  |  |
| 04 |  |  |  |  |
| 05 |  |  |  |  |
| 06 |  |  |  |  |
| 07 |  |  |  |  |
| 08 |  |  |  |  |
| 09 |  |  |  |  |
| 10 |  |  |  |  |
| 11 |  |  |  |  |
| 12 |  |  |  |  |
| 13 |  |  |  |  |
| 14 |  |  |  |  |
| 15 |  |  |  |  |

---

## Cierre de ejecución
- Todos los pasos en OK: [ ]
- Sin faltantes críticos pendientes: [ ]
- Bitácora CSV actualizada: [ ]
- Compilación/validación final ejecutada: [ ]
- Revisión de calidad orientada a Sonar sin hallazgos críticos/bloqueantes nuevos: [ ]

---

## Prellenado — pasos críticos (primera corrida)

### Paso 06 — Adapters output WebClient

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado: `06_generar_adapters_output_webclient.md`
- Cambios esperados: adapters 1:1 por `PortOutput`, inyección de `WebClient` específico por cliente
- Validación ejecutada: compilación módulo + revisión imports adapters
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción: validar ausencia de `WebClient.Builder` genérico y `CommonHttpHeadersOperator`

### Paso 07 — Configuración WebClient

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado: `07_configurar_webclient_output.md`
- Cambios esperados: `WebClientConfig`, `WebClientProperties`, `*WebClientConfig` por cliente
- Validación ejecutada: compilación + binding de propiedades `webclient.*`
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción: validar headers desde `WebClientProperties` sin utilitarios externos

### Paso 09 — Service de orquestación

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado: `09_generar_service_orquestacion_port_input.md`
- Cambios esperados: service `PortInput` delegando a Strategy por `flujo/estado`
- Validación ejecutada: compilación + revisión de resolver/strategies concretas + verificación de `BusinessException` logueada y respuesta funcional con `Mono.just(...)`
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción: eliminar lógica monolítica con `if/switch` sobrepuesta en service principal y confirmar salida funcional correcta para `BusinessException`

### Paso 10 — Controller input

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado: `10_completar_controller_input_adapter_rest.md`
- Cambios esperados: controller actual preservado; ajuste solo de llamada a `PortInput`/strategy
- Validación ejecutada: compilación + verificación de contrato vigente (SOAP/XML o REST) + `onErrorResume` funcional con `Mono.just(...)`
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción: confirmar que no se borró ni reemplazó el controller existente y que `BusinessException` se loguea con `CustomLogLevelHandler`

### Paso 12 — Validaciones legado

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado: `12_generar_validaciones_legado_desde_levantamiento.md`
- Cambios esperados: reglas funcionales desde levantamiento + códigos/mensajes externalizados
- Validación ejecutada: compilación + revisión de mantenibilidad del validador
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción: confirmar patrón declarativo (catálogo/lista de reglas o strategy/specification), sin cadena larga de `if`, y construcción centralizada de `BusinessException`

### Paso 14 — Configuración application.yml

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado: `14_configurar_application_yaml.md`
- Cambios esperados: estructura canónica completa + variables `${...}` en `application.yml` y `applicationlocal.yaml` (sin `${VAR:default}`)
- Validación ejecutada: parse YAML + revisión de placeholders sensibles
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción: alinear variables de `application.yml` y `applicationlocal.yaml` con Helm (`config/secret`) antes de cerrar paso

### Paso 15 — Validación YAML vs Helm

#### Intento 1
- Fecha/Hora:
- Prompt ejecutado: `15_validar_application_yaml_vs_helm_configmap.md`
- Cambios esperados: validación `${...}` de `application.yml` y `applicationlocal.yaml` contra `variables.own.config ∪ variables.own.secret`
- Validación ejecutada: matriz por ambiente (`dev/test/prod`) + faltantes críticos + verificación de ausencia de `${VAR:default}`
- Resultado: [ ] OK [ ] FAIL
- Motivo (si FAIL):
- ¿Se revirtió?: [ ] SI [ ] NO
- Método de reversa (A/B/C):
- Siguiente acción: cerrar únicamente cuando `faltantes_criticos` quede vacío en los 3 ambientes para `APP_VARS_MAIN ∪ APP_VARS_LOCAL`

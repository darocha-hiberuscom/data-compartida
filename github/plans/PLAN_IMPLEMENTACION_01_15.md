# PLAN — IMPLEMENTACIÓN 01–15

> Referencia de secuencia oficial: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## OBJETIVO
Definir una ejecución estructurada por fases para los pasos 01–15, con criterios de avance y control de calidad transversal.

## CHECKLIST OPERATIVO
- Ejecución paso a paso: `.github/plans/CHECKLIST_OPERATIVO_01_15.md`
- Ejecución por intentos: `.github/plans/CHECKLIST_OPERATIVO_01_15_INTENTOS.md`

## FASE 1 — MODELO Y CONTRATOS BASE
- Pasos: `01` a `04`.
- Objetivo: definir DTOs (`record`), puertos de entrada/salida y modelos técnicos request/response.
- Criterio de avance: contratos compilables, convenciones de nombre correctas y trazabilidad completa con levantamiento.

## FASE 2 — INTEGRACIÓN DE SALIDA Y RESILIENCIA TÉCNICA
- Pasos: `05` a `08`.
- Objetivo: mapear dominio↔adapter, implementar adapters con WebClient, wiring y manejo de errores técnicos reactivos.
- Criterio de avance: flujo reactivo consistente (`executeRequest -> onErrorMap -> validateResponse`), sin hardcode de endpoints/credenciales y con patrón obligatorio de `WebClient` específico por cliente (sin `WebClient.Builder` genérico en adapters).

## FASE 3 — ORQUESTACIÓN E INTERFAZ DE ENTRADA
- Pasos: `09` a `11`.
- Objetivo: completar service de orquestación, controller input y presenter/response mapper.
- Criterio de avance: operación end-to-end desde input adapter hasta respuesta final, respetando contratos y política MapStruct.

## FASE 4 — REGLAS FUNCIONALES Y EXCEPCIONES
- Pasos: `12` y `13`.
- Objetivo: implementar validaciones legado y acoplar modelo de excepciones funcional/técnico.
- Criterio de avance: uso correcto de `BusinessException`, diferenciación funcional/técnico y lectura de códigos/mensajes desde `application.yml`.

## FASE 5 — CONFIGURACIÓN Y DESPLIEGUE POR AMBIENTE
- Pasos: `14` y `15`.
- Objetivo: externalizar configuración en `application.yml` y validar consistencia con Helm (`dev/test/prod`).
- Criterio de avance: todas las variables `${...}` (incluyendo `${VAR:default}` normalizada a `VAR`) existen en Helm por ambiente, en `variables.own.config[].name` o `variables.own.secret[].name`, incluyendo convención `*_ERROR_CODE` y `*_ERROR_MESSAGE`.

## ENTREGABLES POR FASE
- Fase 1: contratos base de `domain`, `application.port` y modelos técnicos de output.
- Fase 2: integración de salida WebClient con mappers y manejo de errores técnicos, usando `WebClientConfig` + `WebClientProperties` (`@ConfigurationProperties(prefix = "webclient")`) y beans por cliente.
- Fase 3: capa de entrada completa (controller + presenter + mapper).
- Fase 4: validaciones del legado y excepciones input alineadas al comportamiento histórico.
- Fase 5: configuración final validada contra Helm por ambiente.

## CHECKPOINT FINAL TRANSVERSAL
- Convenciones de nombres (`PortInput`, `PortOutput`, `*AdapterOutput`).
- Política MapStruct obligatoria.
- Flujo reactivo coherente en input/output.
- Sin hardcode de secretos ni códigos/mensajes configurables.
- Evitar al máximo duplicidad de código: reutilizar mappers, estrategias, configuraciones y utilitarios antes de crear nuevos bloques repetidos.
- Prevenir issues de Sonar desde la generación: mantener complejidad controlada, métodos cortos/cohesivos, null-safety, manejo correcto de excepciones y eliminación de código no usado.
- Adapters output inyectan `WebClient` específico por cliente (usar `@Qualifier("<cliente>WebClient")` cuando aplique).
- Configuración de clientes centralizada con `WebClientConfig` y `WebClientProperties` bajo prefijo `webclient`.
- Variables de `application.yml` sincronizadas con `helm/dev.yaml`, `helm/test.yaml`, `helm/prod.yaml`.

### Criterio operativo obligatorio para Paso 15
- Extraer variables de `application.yml` y normalizar placeholders con default.
- Consolidar variables Helm por ambiente: `HELM_VARS = config ∪ secret`.
- Comparar por ambiente:
	- `faltantes_criticos = APP_VARS - HELM_VARS`
	- `sobrantes_informativos = HELM_VARS - APP_VARS`
- Condición de cierre: `faltantes_criticos` vacío en `dev`, `test` y `prod`.

## NOTAS DE USO
- Ejecutar fases en orden y no avanzar con prerequisitos incompletos.
- Si aparece `FALTA DETALLE`, resolver evidencia antes de continuar con fases dependientes.
- Fuente de verdad funcional: `./migracion/LEVANTAMIENTO_INFORMACION.md`.

## MATRIZ RÁPIDA — REGLA CLAVE POR PASO (01–15)

| Paso | Objetivo corto | Regla clave obligatoria | Validación mínima de salida |
|---|---|---|---|
| 01 | DTOs dominio/servicios | Modelado recursivo completo, sin omitir anidados | DTOs request/response compilables y trazables |
| 02 | Puertos de entrada | 1:1 con operaciones del orquestador | Interfaces `*PortInput` completas |
| 03 | Puertos de salida | 1:1 con servicios invocados | Interfaces `*PortOutput` alineadas a levantamiento |
| 04 | Modelos output adapter | `record` request/response por servicio | Clases técnicas sin lógica de negocio |
| 05 | Mappers dominio↔adapter | Política MapStruct obligatoria | Mappers sin mapeo manual innecesario |
| 06 | Adapters WebClient | Inyectar `WebClient` específico por cliente | Sin `WebClient.Builder` genérico ni `CommonHttpHeadersOperator` |
| 07 | Config WebClient | `WebClientConfig` + `WebClientProperties` (`webclient`) | Beans `*WebClientConfig` por cliente + binding válido |
| 08 | Errores técnicos output | `onErrorMap`/`timeout` con excepción de infraestructura | Cobertura 4xx/5xx/timeout/conectividad |
| 09 | Service orquestación | Patrón Strategy por `flujo/estado` | Service delega en resolver + estrategias concretas |
| 10 | Controller input | Mantener controller actual; solo ajustar llamada a `PortInput`/strategy | Contrato vigente preservado (SOAP o REST) |
| 11 | Presenter/response mapper | Separar presentación de orquestación | Response final mapeado sin lógica de negocio en controller |
| 12 | Validaciones legado | Implementar solo reglas evidenciadas | Errores funcionales trazables sin inventar reglas |
| 13 | Excepciones input | Diferenciar funcional vs técnico | `BusinessException` + config externa (`application.yml`) |
| 14 | Configuración app | Estructura canónica YAML obligatoria | `application.yml` completo y externalizado |
| 15 | Validación Helm | Variables `${...}` deben existir en `config ∪ secret` | Sin faltantes críticos en `dev/test/prod` |

## MODO PRUEBA POR PASO (APLICAR / REVERTIR)

### Objetivo
Permitir pruebas iterativas por paso (iniciando en Paso 1), con rollback controlado antes de avanzar al siguiente paso.

### Ciclo recomendado para Paso 1
1. Preparar línea base limpia del código.
2. Ejecutar únicamente el prompt del Paso 1.
3. Validar compilación y/o pruebas mínimas del alcance del Paso 1.
4. Registrar resultado de la prueba.
5. Revertir cambios del Paso 1 para repetir o ajustar.

### Opción A — Reversa rápida (Git, sin conservar cambios)
Usar cuando solo quieres volver al estado previo y no necesitas conservar el intento:

```bash
git restore .
git clean -fd
```

### Opción B — Reversa con trazabilidad (Git, conservando snapshot)
Usar cuando quieres guardar evidencia de cada intento del Paso 1 y poder recuperarlo:

```bash
git add -A
git commit -m "test(paso-1): intento N"

# volver al estado previo al intento
git reset --hard HEAD~1
```

### Opción C — Si no hay repositorio Git disponible en el workspace
Generar respaldo local previo al Paso 1 y restaurar al finalizar la prueba:

```bash
mkdir -p .tmp
tar -czf .tmp/backup-paso-1.tgz src build.gradle settings.gradle

# ... ejecutar y probar Paso 1 ...

tar -xzf .tmp/backup-paso-1.tgz -C .
```

### Reglas para probar pasos secuenciales
- No ejecutar Paso 2 mientras Paso 1 no pase validación mínima.
- Cada paso se prueba en aislamiento: aplicar → validar → revertir (o consolidar) → siguiente paso.
- Si el resultado del paso es aceptado, consolidar antes de continuar (commit o baseline explícita).
- Mantener un registro por intento: paso, fecha, resultado, motivo de reversa.

### Plantilla de bitácora de intentos (recomendada)
Usar esta tabla para registrar cada prueba de forma trazable:

Archivo sugerido para llenado operativo: `migracion/bitacora_intentos_pasos.csv`.

| Intento | Paso | Fecha/Hora | Prompt ejecutado | Validación ejecutada | Resultado (OK/FAIL) | ¿Se revirtió? | Método de reversa | Motivo de rollback | Observaciones / siguiente acción |
|---|---|---|---|---|---|---|---|---|---|
| 1 | 01 | 2026-05-15 10:00 | `01_*` | `./gradlew test` (subset) | FAIL | SI | Git A | Error de compilación DTO | Ajustar nombres y reintentar |
| 2 | 01 | 2026-05-15 10:25 | `01_*` | `./gradlew test` (subset) | OK | NO | N/A | N/A | Consolidar baseline para Paso 2 |

Checklist mínimo por intento:
- Confirmar baseline limpio antes de ejecutar el paso.
- Ejecutar solo el prompt del paso en prueba (sin mezclar pasos).
- Registrar comando(s) de validación realmente ejecutados.
- Registrar explícitamente si se revirtió y con qué método (A/B/C).

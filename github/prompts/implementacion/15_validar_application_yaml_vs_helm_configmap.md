````markdown
# IMPLEMENTACIÓN 15 — VALIDAR APPLICATION.YML/APPLICATIONLOCAL.YAML VS HELM (DEV/TEST/PROD)

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 14 completado (`application.yml` configurado).
- Existen archivos Helm por ambiente:
  - `repos/destino/${nombre_proyecto}/helm/dev.yaml`
  - `repos/destino/${nombre_proyecto}/helm/test.yaml`
  - `repos/destino/${nombre_proyecto}/helm/prod.yaml`

## OBJETIVO
Validar y garantizar que todas las variables usadas en `application.yml` y `applicationlocal.yaml` existan en Helm (`dev/test/prod`) dentro de `variables.own.config` o `variables.own.secret`, según sensibilidad.

## ALCANCE
- Leer y comparar:
  - `repos/destino/${nombre_proyecto}/src/main/resources/application.yml`
  - `repos/destino/${nombre_proyecto}/src/main/resources/applicationlocal.yaml`
  - `repos/destino/${nombre_proyecto}/helm/dev.yaml`
  - `repos/destino/${nombre_proyecto}/helm/test.yaml`
  - `repos/destino/${nombre_proyecto}/helm/prod.yaml`
- Ajustar valores Helm para cubrir variables faltantes en cada ambiente.

## INSTRUCCIONES

### 1) Extraer variables desde application.yml y applicationlocal.yaml
Identificar todas las referencias con patrón:
- `${VARIABLE_ENV}`
- `${VARIABLE_ENV:default}`

Generar listado único de variables requeridas por la aplicación.

Reglas obligatorias:
- Validar ambos archivos por separado y en conjunto:
  - `APP_VARS_MAIN` desde `application.yml`
  - `APP_VARS_LOCAL` desde `applicationlocal.yaml`
  - `APP_VARS = APP_VARS_MAIN ∪ APP_VARS_LOCAL`
- En ambos archivos (`application.yml` y `applicationlocal.yaml`) está prohibido usar defaults en placeholders (`${VAR:default}`).

Exclusión obligatoria:
- No considerar ni generar variables `BSS_APIM_OAUTH2_PROVIDER`, `BSS_APIM_OAUTH2_CLIENT_ID`, `BSS_APIM_OAUTH2_CLIENT_SECRET`, `BSS_APIM_OAUTH2_AUDIENCE`, `BSS_APIM_OAUTH2_API_KEY`.
- No considerar ni generar variables `WS_TRANSACTION_JOURNAL_LOCAL_DATE_PATTERN`, `WS_ACCOUNT_TRANSACTION_LOCAL_DATE_PATTERN`, `WS_DEPOSIT_LOCAL_DATE_PATTERN`, `LOCATION_MANAGEMENT_INTERVAL_DELAY`, `LOCATION_MANAGEMENT_INTERVAL_PERIOD`, `LOCATION_CAFFEINE_CACHE_MAX_SIZE`, `LOCATION_CAFFEINE_CACHE_EXPIRE_AFTER_WRITE`, `LOCATION_RETRY_MAX_ATTEMPTS`, `LOCATION_RETRY_FIXED_DELAY`, `BSS_APIM_LOCATION_SERVICE_PATH`, `BSS_APIM_LOCATION_OAUTH2_CLIENT_REGISTRATION_ID`.
- Si existen en Helm por arrastre histórico, reportarlas como sobrantes informativos y no recrearlas.

### 2) Extraer variables desde Helm por ambiente
Para cada archivo (`dev.yaml`, `test.yaml`, `prod.yaml`), leer:
- `variables.own.config[].name`
- `variables.own.secret[].name`

Si `repos/destino/${nombre_proyecto}/helm` no existe, registrar `FALTA DETALLE` con pista trazable y usar ruta alternativa solo si está evidenciada.

Generar listado consolidado por ambiente (`config ∪ secret`).

### 3) Validar consistencia
Comparar por ambiente:
- Variables en `application.yml` o `applicationlocal.yaml` y ausentes en Helm (crítico).
- Variables en Helm y no usadas por `application.yml` (informativo).

Comparación informativa recomendada adicional:
- Variables en Helm y no usadas por `application.yml` ni `applicationlocal.yaml`.

Validación obligatoria adicional:
- Variables de códigos/mensajes de error usadas por la app (convención `*_ERROR_CODE` y `*_ERROR_MESSAGE`) deben existir en `dev/test/prod`.

Reglas obligatorias:
- Toda variable crítica faltante debe agregarse en el Helm del ambiente.
- Mantener estructura existente (`variables.own.config` y `variables.own.secret`).

### 3.1) Ejemplo operativo de extracción/comparación (formato estándar)
Usar este flujo mínimo y el mismo formato de salida en cada ejecución:

1. **Extraer desde application.yml y applicationlocal.yaml**
  - Detectar placeholders `${VAR}` y `${VAR:default}`.
  - Normalizar a nombre de variable sin default (`VAR`).
  - Resultado:
    - `APP_VARS_MAIN` (desde `application.yml`)
    - `APP_VARS_LOCAL` (desde `applicationlocal.yaml`)
    - `APP_VARS = APP_VARS_MAIN ∪ APP_VARS_LOCAL`

2. **Extraer desde Helm por ambiente**
  - `HELM_CONFIG_VARS = variables.own.config[].name`
  - `HELM_SECRET_VARS = variables.own.secret[].name`
  - `HELM_VARS = HELM_CONFIG_VARS ∪ HELM_SECRET_VARS`

3. **Comparar por ambiente (`dev`, `test`, `prod`)**
  - `faltantes_criticos = APP_VARS - HELM_VARS`
  - `sobrantes_informativos = HELM_VARS - APP_VARS`

4. **Salida obligatoria por ambiente**
  - `ambiente: dev|test|prod`
  - `total_app_vars: <n>`
  - `total_helm_vars: <n>`
  - `faltantes_criticos: [VAR_1, VAR_2, ...]`
  - `sobrantes_informativos: [VAR_X, VAR_Y, ...]`

5. **Criterio de éxito**
  - `faltantes_criticos` debe quedar vacío en `dev/test/prod`.

### 4) Ajustar Helm
Agregar en cada `dev/test/prod` las variables faltantes con:
- `name`: variable exacta
- `value`: placeholder o valor correspondiente por ambiente

Clasificación obligatoria:
- No sensible -> agregar en `variables.own.config` (`name` + `value`).
- Sensible (secretos, credenciales, api-keys, passwords, client-secret) -> agregar en `variables.own.secret` (`name` + `location`).

Reglas:
- No inventar valores productivos no evidenciados.
- Para valores desconocidos usar placeholder trazable y marcar `FALTA DETALLE` en documentación de despliegue.

### 5) Estándar técnico compartido
Aplicar exactamente las reglas de:
- `.github/prompts/shared/PROMPT_BASE_HELM_CONFIGMAP_SYNC.md`

## SALIDA OBLIGATORIA
- Validación de consistencia `application.yml` y `applicationlocal.yaml` vs Helm completada para `dev/test/prod`.
- Archivos Helm ajustados sin faltantes críticos de variables.

## CHECKLIST DE VALIDACIÓN
- [ ] Variables `${...}` de `application.yml` extraídas y listadas
- [ ] Variables `${...}` de `applicationlocal.yaml` extraídas y listadas
- [ ] `application.yml` no contiene placeholders con default (`${VAR:default}`)
- [ ] `applicationlocal.yaml` no contiene placeholders con default (`${VAR:default}`)
- [ ] Variables `variables.own.config[].name` y `variables.own.secret[].name` extraídas para `dev/test/prod`
- [ ] Comparación por ambiente ejecutada
- [ ] Sin faltantes críticos (`application.yml ∪ applicationlocal.yaml` -> `helm`)
- [ ] Sin faltantes de variables de error/código/mensaje en `dev/test/prod`
- [ ] No se exigieron ni generaron variables `BSS_APIM_OAUTH2_*`
- [ ] No se exigieron ni generaron variables `WS_*_LOCAL_DATE_PATTERN`, `LOCATION_*`, `BSS_APIM_LOCATION_*`
- [ ] Estructura YAML de Helm preservada (`variables.own.config` / `variables.own.secret`)
- [ ] Sin secretos hardcodeados en archivos de valores

## REGLAS
- NO omitir variables requeridas por `application.yml` o `applicationlocal.yaml`.
- NO hardcodear secretos sensibles.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_APPLICATION_YAML_CONFIG.md`
  - `.github/prompts/shared/PROMPT_BASE_HELM_CONFIGMAP_SYNC.md`

````
```markdown
# PROMPT BASE — SINCRONIZACIÓN APPLICATION.YML ↔ HELM (CONFIG + SECRET)

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la validación de consistencia entre variables usadas en `application.yml` y variables declaradas en Helm (`dev.yaml`, `test.yaml`, `prod.yaml`) en secciones `variables.own.config` y `variables.own.secret`.

## Reglas obligatorias
- Toda variable referenciada en `application.yml` mediante `${VAR_NAME}` o `${VAR_NAME:default}` debe existir en Helm para cada ambiente (`dev`, `test`, `prod`) en al menos una de estas rutas:
   - `variables.own.config[].name`
   - `variables.own.secret[].name`
- No eliminar variables existentes en Helm sin evidencia funcional.
- Mantener nombres de variables en mayúsculas y snake case según estándar existente.
- No hardcodear secretos en `application.yml`; usar placeholders y provisionar en Helm/secret según corresponda.
- Códigos y mensajes de error referenciados por `application.yml` son obligatorios en Helm por ambiente.

## Fuentes de validación
- `repos/destino/${nombre_proyecto}/src/main/resources/application.yml`
- `repos/destino/${nombre_proyecto}/helm/dev.yaml`
- `repos/destino/${nombre_proyecto}/helm/test.yaml`
- `repos/destino/${nombre_proyecto}/helm/prod.yaml`

## Regla de comparación
1. Extraer variables de `application.yml` con patrón `${...}`.
   - Si el placeholder tiene default (`${VAR:default}`), tomar solo `VAR` para validar existencia en Helm.
2. Extraer variables de Helm en:
   - `variables.own.config[].name`
   - `variables.own.secret[].name`
   - Consolidar ambos listados en un set único por ambiente.
3. Detectar faltantes por ambiente:
   - `en_application_no_en_helm`
   - `en_helm_no_en_application` (informativo)

## Resultado esperado
- Matriz de validación por ambiente (`dev/test/prod`) sin faltantes críticos.
- Si hay faltantes:
   - agregar en `variables.own.config` cuando sea no sensible,
   - agregar en `variables.own.secret` cuando sea secreto (credenciales, api-keys, passwords, client-secret, tokens sensibles).
- Sin faltantes de variables de error con convención `*_ERROR_CODE` y `*_ERROR_MESSAGE` usadas por la aplicación.

## Alcance
Aplicable a despliegues que usen Helm values por ambiente con estructura:
- `variables.own.config`
- `variables.own.secret`

```
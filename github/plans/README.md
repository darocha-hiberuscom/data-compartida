# Planes

Repositorio de planes operativos para ejecución de levantamiento e implementación.

## Índice
- [Plan de Implementación 01–15](./PLAN_IMPLEMENTACION_01_15.md)
  - Uso: ejecución estructurada por fases para los pasos de implementación.
- [Checklist Operativo 01–15](./CHECKLIST_OPERATIVO_01_15.md)
  - Uso: control de ejecución real por paso con validación mínima y resultado OK/FAIL.
- [Checklist Operativo por Intentos 01–15](./CHECKLIST_OPERATIVO_01_15_INTENTOS.md)
  - Uso: control por Intento 1/2/3 por paso, integrado con la bitácora CSV.
- [Plan de Levantamiento Orquestador](./plan_levantamiento_orquestador.md)
  - Uso: guía de levantamiento y consolidación de evidencia funcional/técnica.

## Estado de planes
| Plan | Estado | Última actualización | Aprobador |
|---|---|---|---|
| [Plan de Implementación 01–15](./PLAN_IMPLEMENTACION_01_15.md) | active | 2026-05-15 | Equipo Migración (Aprobación colegiada) |
| [Plan de Levantamiento Orquestador](./plan_levantamiento_orquestador.md) | active | 2026-05-15 | Equipo Migración (Aprobación colegiada) |

Última verificación del índice: 2026-05-15.

Estados permitidos: `draft`, `active`, `done`.

## Matriz de transición de estado
| Estado actual | Siguiente estado | Condición mínima |
|---|---|---|
| `draft` | `active` | Plan aprobado para ejecución y con fases definidas. |
| `active` | `done` | Todas las fases/checkpoints completados y validados. |
| `done` | `active` | Se reabre el plan por cambio de alcance o retrabajo evidenciado. |

## Reglas de uso
- Mantener un archivo por plan principal.
- Nombrar archivos con objetivo claro y alcance explícito.
- Actualizar este índice cuando se cree, renombre o elimine un plan.
- Usar formato de fecha `YYYY-MM-DD` en la columna **Última actualización**.
- Para planes por fases, actualizar **Última actualización** al cerrar cada fase.
- Cambiar estado a `done` solo cuando todas las fases/checkpoints del plan estén completados.
- Registrar **Aprobador** en cada cambio de estado (`draft`, `active`, `done`).
- Criterio de **Aprobador**:
  - Usar nombre de persona cuando la aprobación sea individual (ejemplo: Tech Lead responsable).
  - Usar nombre de equipo cuando la aprobación sea colegiada o rotativa (ejemplo: Equipo Migración).

Formato sugerido para columna **Aprobador**:
- Individual: `Nombre Apellido (Rol)`
  - Ejemplo: `Nombre Apellido (Tech Lead)`
- Equipo: `Nombre Equipo (Tipo)`
  - Ejemplo: `Equipo Migración (Aprobación colegiada)`

## Estándar transversal de configuración
- Para despliegue por ambiente, toda variable de application.yml debe existir en Helm de dev/test/prod.
- Validar contra ambos nodos: variables.own.config[].name y variables.own.secret[].name.
- Soportar placeholders ${VAR} y ${VAR:default}; normalizar a VAR antes de comparar.
- Comparación por ambiente:
  - faltantes_criticos = APP_VARS - HELM_VARS
  - sobrantes_informativos = HELM_VARS - APP_VARS
- Cierre del control: faltantes_criticos vacío en dev, test y prod.
- Alta de variables faltantes:
  - no sensibles en variables.own.config (name + value)
  - sensibles en variables.own.secret (name + location)

## Estándar transversal de WebClient (pasos 06–08)
- Los adapters output deben inyectar bean `WebClient` específico por cliente (ejemplo: `customerPaymentsWebClient`).
- No usar `WebClient.Builder` genérico en adapters.
- Si existen múltiples beans `WebClient`, usar `@Qualifier("<cliente>WebClient")`.
- La configuración de clientes debe centralizarse con `WebClientConfig` + `WebClientProperties`.
- `WebClientProperties` debe mapear desde `application.yml` con `@ConfigurationProperties(prefix = "webclient")`.

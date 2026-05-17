# CHECKLIST OPERATIVO — IMPLEMENTACIÓN 01–15

> Uso: ejecución paso a paso en corrida real de migración.
> Referencias:
> - `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`
> - `.github/plans/PLAN_IMPLEMENTACION_01_15.md`

## Reglas de ejecución
- Ejecutar estrictamente en orden (`01` → `15`).
- No avanzar si el paso actual no cumple su validación mínima.
- Si hay vacío de evidencia, registrar `FALTA DETALLE` + `Pista`.
- Registrar resultado por intento en `migracion/bitacora_intentos_pasos.csv`.

---

## Paso 01 — DTOs
- [ ] Prompt ejecutado (`01_generar_dtos_desde_levantamiento.md`)
- [ ] DTOs request/response generados
- [ ] Modelado recursivo completo (sin anidados faltantes)
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 02 — Puertos de entrada
- [ ] Prompt ejecutado (`02_generar_puertos_entrada.md`)
- [ ] Interfaces `*PortInput` 1:1 con operaciones
- [ ] Firmas alineadas a DTOs del paso 01
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 03 — Puertos de salida
- [ ] Prompt ejecutado (`03_generar_puertos_salida.md`)
- [ ] Interfaces `*PortOutput` 1:1 con servicios invocados
- [ ] Sin dependencias a adapters concretos
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 04 — Modelos output adapter
- [ ] Prompt ejecutado (`04_generar_modelos_adapter_output_request_response.md`)
- [ ] Records request/response por servicio
- [ ] Tipos y anidación coherentes con levantamiento
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 05 — Mappers dominio↔adapter
- [ ] Prompt ejecutado (`05_generar_mappers_dominio_adapter_output.md`)
- [ ] Política MapStruct aplicada
- [ ] Sin mapeo manual innecesario
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 06 — Adapters output WebClient
- [ ] Prompt ejecutado (`06_generar_adapters_output_webclient.md`)
- [ ] Adapter por `PortOutput`
- [ ] Inyección de `WebClient` específico por cliente
- [ ] No usa `WebClient.Builder` genérico
- [ ] No genera/usa `CommonHttpHeadersOperator`
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 07 — Configuración WebClient
- [ ] Prompt ejecutado (`07_configurar_webclient_output.md`)
- [ ] Existe `WebClientConfig` base
- [ ] Existe `WebClientProperties` con prefijo `webclient`
- [ ] Existen `*WebClientConfig` por cliente
- [ ] Headers configurados desde properties (sin operadores auxiliares)
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 08 — Errores técnicos output
- [ ] Prompt ejecutado (`08_manejo_errores_tecnicos_output_webclient.md`)
- [ ] Cobertura de errores `4xx`, `5xx`, timeout y conectividad
- [ ] `onErrorMap` consistente por adapter
- [ ] Excepciones técnicas mapeadas a infraestructura
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 09 — Service de orquestación
- [ ] Prompt ejecutado (`09_generar_service_orquestacion_port_input.md`)
- [ ] Service implementa `PortInput`
- [ ] Patrón Strategy por `flujo/estado` aplicado
- [ ] Resolver + estrategias concretas creadas
- [ ] Sin lógica monolítica sobrepuesta
- [ ] Error funcional genera `BusinessException` y se loguea con `CustomLogLevelHandler`
- [ ] `BusinessException` se responde como salida funcional correcta (`Mono.just(...)`)
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 10 — Controller input
- [ ] Prompt ejecutado (`10_completar_controller_input_adapter_rest.md`)
- [ ] Controller actual preservado (sin borrar/reemplazar)
- [ ] Solo se ajusta llamada a `PortInput`/strategy
- [ ] Contrato vigente preservado (SOAP/XML o REST)
- [ ] `BusinessException` se loguea con `CustomLogLevelHandler`
- [ ] `onErrorResume` retorna `Mono.just(...)` para respuesta funcional de negocio
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 11 — Presenter/response mapper input
- [ ] Prompt ejecutado (`11_generar_presenter_response_mapper_input.md`)
- [ ] Mapper de respuesta implementado
- [ ] Separación controller/presenter mantenida
- [ ] Presenter transforma `BusinessException` en respuesta funcional correcta
- [ ] Respuesta funcional de negocio se resuelve con `Mono.just(...)`
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 12 — Validaciones legado
- [ ] Prompt ejecutado (`12_generar_validaciones_legado_desde_levantamiento.md`)
- [ ] Reglas funcionales implementadas solo con evidencia
- [ ] Códigos/mensajes no hardcodeados
- [ ] Patrón mantenible aplicado (catálogo/lista de reglas o strategy/specification)
- [ ] No existe cadena larga de `if` secuenciales para validar campos
- [ ] Construcción de `BusinessException` centralizada/reutilizable
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 13 — Modelo de excepciones input
- [ ] Prompt ejecutado (`13_acoplar_modelo_excepciones_input.md`)
- [ ] Distinción funcional vs técnico correcta
- [ ] Códigos/mensajes leídos desde `application.yml`
- [ ] `BusinessException` se loguea con `CustomLogLevelHandler` (nivel `ERROR`)
- [ ] Rama funcional en `onErrorResume` retorna `Mono.just(respuesta de negocio)`
- [ ] Compila sin errores
- Resultado: [ ] OK [ ] FAIL

## Paso 14 — Configuración application.yml
- [ ] Prompt ejecutado (`14_configurar_application_yaml.md`)
- [ ] Estructura canónica completa aplicada
- [ ] Variables externalizadas `${...}` en `application.yml` y `applicationlocal.yaml`
- [ ] Sin placeholders con default (`${VAR:default}`) en `application.yml` y `applicationlocal.yaml`
- [ ] Variables de `application.yml` y `applicationlocal.yaml` existen en Helm (`variables.own.config[].name ∪ variables.own.secret[].name`)
- [ ] Sin secretos hardcodeados
- Resultado: [ ] OK [ ] FAIL

## Paso 15 — Validación YAML vs Helm
- [ ] Prompt ejecutado (`15_validar_application_yaml_vs_helm_configmap.md`)
- [ ] Variables `${...}` extraídas de `application.yml` y `applicationlocal.yaml`
- [ ] Validación de ausencia de `${VAR:default}` en ambos archivos
- [ ] Validación contra `variables.own.config[].name ∪ variables.own.secret[].name`
- [ ] Sin `faltantes_criticos` en `dev/test/prod` para `APP_VARS_MAIN ∪ APP_VARS_LOCAL`
- Resultado: [ ] OK [ ] FAIL

---

## Cierre de corrida
- [ ] Todos los pasos 01–15 en estado `OK`
- [ ] Sin `FALTA DETALLE` bloqueante pendiente
- [ ] Bitácora `migracion/bitacora_intentos_pasos.csv` actualizada
- [ ] Validación final de compilación ejecutada
- [ ] Revisión de calidad orientada a Sonar sin hallazgos críticos/bloqueantes nuevos

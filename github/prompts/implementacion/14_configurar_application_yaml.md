````markdown
# IMPLEMENTACIÓN 14 — CONFIGURACIÓN DE APPLICATION.YML

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 06 completado (adapters output con WebClient).
- Paso 07 completado (wiring/configuración WebClient).
- Paso 13 completado (modelo de excepciones input acoplado).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.

## OBJETIVO
Configurar `application.yml` del proyecto migrado usando la estructura canónica aprobada por el usuario (bloques `spring`, `logging`, `webclient`) y propiedades externalizadas, además de generar `applicationlocal.yaml` para pruebas locales usando variables (sin valores por defecto).

## ALCANCE
- Crear/actualizar:
  - `repos/destino/${nombre_proyecto}/src/main/resources/application.yml`
  - `repos/destino/${nombre_proyecto}/src/main/resources/applicationlocal.yaml`
- Generar configuración respetando exactamente la jerarquía definida en:
  - `.github/prompts/shared/PROMPT_BASE_APPLICATION_YAML_CONFIG.md`

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Usar como formato base de configuración:
- patrón YAML canónico provisto por el usuario (estructura completa).

Si hay inconsistencias:
- preferir evidencia trazable,
- registrar `FALTA DETALLE` en comentario de configuración (si aplica en archivo de soporte/documentación).

### 2) Aplicación de estructura canónica
Aplicar bloque por bloque la estructura definida en el prompt base, incluyendo:
- `spring.main/application/header/kafka`
- `logging.level/event/excluded-paths/executor`
- `xml.template.templates`
- `webclient` (todos los sub-bloques requeridos)

Reglas:
- Mantener el orden y jerarquía del patrón canónico.
- Usar placeholders `${...}` para datos de ambiente.
- En `application.yml` usar solo formato `${VARIABLE}` sin valores por defecto (`${VARIABLE:default}` prohibido).
- En `application.yml` incluir únicamente variables `${VARIABLE}` que estén referenciadas en Helm (`variables.own.config[].name ∪ variables.own.secret[].name`) para el servicio.
- Si una variable requerida por la estructura canónica no existe en Helm, registrar `FALTA DETALLE` y no inventar placeholder fuera del set Helm.
- El bloque `spring.kafka` debe quedar exactamente con:
  - `security.protocol: PLAINTEXT`
  - `properties.sasl.mechanism: PLAIN`
  - `properties.sasl.jaas.config: ""`
  - `properties.bootstrap.servers: ${KAFKA_SERVER}`
  - sin `properties.security.protocol`
- No incluir bloque `spring.config.import` con `card-types.yml` ni `visa-catalog.yml`.
- El bloque `logging` debe quedar exactamente con:
  - `logging.level.org.apache.kafka: OFF`
  - `logging.event.mode: 'EXTERNAL'`
  - `logging.event.kafka.topic.name: ${KAFKA_TOPIC_AUDITOR}`
  - `logging.excluded-paths: /actuator/**,/health,/metrics,/prometheus`
  - `logging.executor` con `isDefault`, `corePoolSize`, `maxPoolSize`, `keepAliveTime`, `queueSize`
- El bloque `xml` debe quedar exactamente con:
  - `xml.template.templates."TipoTransaccion": ${XML_TRANSACCION_0001}`
- No incluir bloque `spring.security.oauth2.client.registration` en `application.yml`.
- No generar variables `BSS_APIM_OAUTH2_PROVIDER`, `BSS_APIM_OAUTH2_CLIENT_ID`, `BSS_APIM_OAUTH2_CLIENT_SECRET`, `BSS_APIM_OAUTH2_AUDIENCE`, `BSS_APIM_OAUTH2_API_KEY`.
- No generar variables antiguas de Kafka (`TRE_KAFKA_SERVER`, `SPRING_KAFKA_PROPERTIES_SECURITY_PROTOCOL`, `ONB_KAFKA_USERNAME`, `ONB_KAFKA_SECRET`).
- No incluir secciones top-level `transaction-journal`, `account-transaction`, `deposit`, `location-management` en `application.yml`.
- No generar variables `WS_TRANSACTION_JOURNAL_LOCAL_DATE_PATTERN`, `WS_ACCOUNT_TRANSACTION_LOCAL_DATE_PATTERN`, `WS_DEPOSIT_LOCAL_DATE_PATTERN`, `LOCATION_MANAGEMENT_INTERVAL_DELAY`, `LOCATION_MANAGEMENT_INTERVAL_PERIOD`, `LOCATION_CAFFEINE_CACHE_MAX_SIZE`, `LOCATION_CAFFEINE_CACHE_EXPIRE_AFTER_WRITE`, `LOCATION_RETRY_MAX_ATTEMPTS`, `LOCATION_RETRY_FIXED_DELAY`, `BSS_APIM_LOCATION_SERVICE_PATH`, `BSS_APIM_LOCATION_OAUTH2_CLIENT_REGISTRATION_ID`.
- No inventar claves fuera del patrón sin evidencia de levantamiento.

### 3) Generación de applicationlocal.yaml para pruebas
Crear `applicationlocal.yaml` con la misma estructura funcional requerida por el servicio, usando variables `${VARIABLE}` (sin valores por defecto) alineadas a Helm.

Reglas obligatorias:
- `applicationlocal.yaml` debe contener placeholders `${VARIABLE}` y no debe contener `${VARIABLE:default}`.
- `applicationlocal.yaml` debe reutilizar exactamente el mismo set de variables presente en Helm (prioridad: `dev`) para el servicio.
- No agregar variables nuevas en `applicationlocal.yaml` que no estén en Helm.
- No usar credenciales reales de producción.

### 4) Parametrización para cualquier servicio
Al reutilizar el patrón en otro servicio, reemplazar de forma obligatoria:
- `spring.application.name` por el nombre del servicio migrado.

No cambiar la jerarquía ni romper compatibilidad con librerías transversales (logging/auditoría/kafka/webclient).

### 5) Estándar técnico compartido
Aplicar exactamente las reglas de:
- `.github/prompts/shared/PROMPT_BASE_APPLICATION_YAML_CONFIG.md`

## SALIDA OBLIGATORIA
- `application.yml` actualizado con la estructura canónica completa definida por el usuario.
- `applicationlocal.yaml` generado para pruebas locales con variables `${VARIABLE}` (sin default).
- Configuración externalizada sin secretos hardcodeados.

## CHECKLIST DE VALIDACIÓN
- [ ] Existe bloque `spring` con `main`, `application`, `header`, `security`, `kafka`
- [ ] Existe bloque `logging` con `level`, `event`, `excluded-paths`, `executor`
- [ ] Existe bloque `xml` con `template.templates."TipoTransaccion"`
- [ ] Existe bloque `webclient` con sub-bloques requeridos por el servicio
- [ ] `application.yml` no contiene `spring.config.import` de `card-types.yml` / `visa-catalog.yml`
- [ ] `application.yml` no contiene placeholders con default (`${VAR:default}`)
- [ ] `application.yml` solo usa variables existentes en Helm (`config ∪ secret`)
- [ ] `application.yml` no contiene bloque `spring.security.oauth2.client.registration`
- [ ] No se generaron variables `BSS_APIM_OAUTH2_*` en `application.yml` ni en Helm
- [ ] `application.yml` no contiene secciones top-level `transaction-journal`, `account-transaction`, `deposit`, `location-management`
- [ ] No se generaron variables `WS_*_LOCAL_DATE_PATTERN`, `LOCATION_*`, `BSS_APIM_LOCATION_*`
- [ ] Existe `applicationlocal.yaml` y usa placeholders `${VARIABLE}` sin default
- [ ] `applicationlocal.yaml` solo usa variables existentes en Helm (`config ∪ secret`)
- [ ] Se reemplazaron identificadores del ejemplo por los del servicio real
- [ ] Variables sensibles externalizadas con `${...}`
- [ ] Sin hardcode de secretos
- [ ] YAML válido y sin errores de estructura

## REGLAS
- NO inventar valores de infraestructura no evidenciados.
- NO incluir credenciales en texto plano.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_APPLICATION_YAML_CONFIG.md`

````
```markdown
# PROMPT BASE — CONFIGURACIÓN APPLICATION.YML

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la configuración de `application.yml` para la solución migrada, usando propiedades externas y formato alineado al estándar del proyecto.

## Reglas obligatorias
- Mantener configuración externalizada vía variables de entorno (`${...}`).
- No hardcodear secretos ni valores sensibles.
- Conservar estructura YAML jerárquica clara y consistente.
- Ajustar solo claves necesarias para la migración, sin introducir bloques no requeridos.
- Códigos y mensajes de error configurables deben declararse como propiedades referenciadas por `${...}`.
- En `application.yml` usar únicamente placeholders sin default: `${VARIABLE}` (prohibido `${VARIABLE:default}`).
- No incluir bloque `spring.config.import` con `classpath:card-types.yml` o `classpath:visa-catalog.yml`.
- Generar `applicationlocal.yaml` para pruebas locales con valores literales (sin `${...}`), tomando como base valores de Helm (preferencia `dev`).

## Bloques de configuración esperados
- `spring.kafka` (security/properties/producer).
- `logging` (niveles, event mode, kafka topic, excluded paths, executor).
- `xml.template.templates` (plantillas XML parametrizadas).
- bloque(s) de errores funcionales/técnicos del servicio con variables de entorno (ej: `${HEADER_INVALID_ERROR_CODE}`, `${HEADER_INVALID_ERROR_MESSAGE}`, `${TIMEOUT_ERROR_CODE}`, `${TIMEOUT_ERROR_MESSAGE}`).
- Archivo adicional `applicationlocal.yaml` con estructura equivalente para ejecución local sin variables de entorno.

## Plantilla mínima sugerida (errores)
```yaml
service-errors:
	````markdown
	```markdown
	# PROMPT BASE — CONFIGURACIÓN APPLICATION.YML (ESTRUCTURA CANÓNICA)

	> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

	## Objetivo
	Generar `application.yml` con **estructura canónica fija** basada en el patrón aprobado por el usuario, externalizando valores con `${...}` y reutilizable para cualquier servicio.

	## Reglas obligatorias
	- Mantener exactamente la jerarquía de bloques del formato canónico: `spring`, `logging`, `webclient`.
	- No hardcodear secretos ni valores sensibles.
	- Solo usar placeholders `${ENV_VAR}` para valores dependientes de ambiente en `application.yml` (sin defaults).
	- Reemplazar identificadores del ejemplo por el servicio actual (nombre de aplicación, paquete base, component.name).
	- No agregar bloques fuera del formato solicitado salvo requerimiento explícito del levantamiento.
	- No incluir `spring.config.import` de `card-types.yml` / `visa-catalog.yml`.
	- No incluir bloque `spring.security.oauth2.client.registration` en `application.yml`.
	- No generar variables `BSS_APIM_OAUTH2_PROVIDER`, `BSS_APIM_OAUTH2_CLIENT_ID`, `BSS_APIM_OAUTH2_CLIENT_SECRET`, `BSS_APIM_OAUTH2_AUDIENCE`, `BSS_APIM_OAUTH2_API_KEY`.
	- No incluir secciones top-level `transaction-journal`, `account-transaction`, `deposit`, `location-management` en `application.yml`.
	- No generar variables `WS_TRANSACTION_JOURNAL_LOCAL_DATE_PATTERN`, `WS_ACCOUNT_TRANSACTION_LOCAL_DATE_PATTERN`, `WS_DEPOSIT_LOCAL_DATE_PATTERN`, `LOCATION_MANAGEMENT_INTERVAL_DELAY`, `LOCATION_MANAGEMENT_INTERVAL_PERIOD`, `LOCATION_CAFFEINE_CACHE_MAX_SIZE`, `LOCATION_CAFFEINE_CACHE_EXPIRE_AFTER_WRITE`, `LOCATION_RETRY_MAX_ATTEMPTS`, `LOCATION_RETRY_FIXED_DELAY`, `BSS_APIM_LOCATION_SERVICE_PATH`, `BSS_APIM_LOCATION_OAUTH2_CLIENT_REGISTRATION_ID`.

	## Estructura canónica requerida
	Usar esta estructura como base y ajustarla al servicio objetivo:

	```yaml
	spring:
		main:
			lazy-initialization: on
		application:
			name: ${SERVICE_APPLICATION_NAME}
		header:
			channel: digital
			medium: web
		kafka:
			security:
				protocol: PLAINTEXT
			properties:
				sasl:
					mechanism: PLAIN
					jaas:
						config: ""
				bootstrap:
					servers: ${KAFKA_SERVER}
				session:
					timeout:
						ms: 45000
				request:
					timeout:
						ms: 2000
			producer:
				key-serializer: org.apache.kafka.common.serialization.StringSerializer
				value-serializer: org.apache.kafka.common.serialization.StringSerializer

	logging:
		level:
			org:
				apache:
					kafka: OFF
		event:
			mode: 'EXTERNAL'
			kafka:
				topic:
					name: ${KAFKA_TOPIC_AUDITOR}
		excluded-paths: /actuator/**,/health,/metrics,/prometheus
		executor:
			isDefault: false
			corePoolSize: ${THREAD_CORE_POOL_SIZE}
			maxPoolSize: ${THREAD_MAX_POOL_SIZE}
			keepAliveTime: ${THREAD_KEEP_ALIVE_TIME}
			queueSize: ${THREAD_QUEUE_SIZE}

	xml:
		template:
			templates:
				"TipoTransaccion": ${XML_TRANSACCION_0001}

	webclient:
		transaction-journal:
			connection-timeout: ${WS_TRANSACTION_JOURNAL_WEBCLIENT_CONNECTION_TIMEOUT}
			read-timeout: ${WS_TRANSACTION_JOURNAL_WEBCLIENT_READ_TIMEOUT}
			max-connections: ${WS_TRANSACTION_JOURNAL_WEBCLIENT_MAX_CONNECTIONS}
			pending-acquire-max-count: ${WS_TRANSACTION_JOURNAL_WEBCLIENT_PENDING_ACQUIRE_MAX_COUNT}
			uri: ${WS_TRANSACTION_JOURNAL_WEBCLIENT_URI}
		deposit-detail:
			connection-timeout: ${WS_DEPOSIT_DETAIL_WEBCLIENT_CONNECTION_TIMEOUT}
			read-timeout: ${WS_DEPOSIT_DETAIL_WEBCLIENT_READ_TIMEOUT}
			max-connections: ${WS_DEPOSIT_DETAIL_WEBCLIENT_MAX_CONNECTIONS}
			pending-acquire-max-count: ${WS_DEPOSIT_DETAIL_WEBCLIENT_PENDING_ACQUIRE_MAX_COUNT}
			uri: ${WS_DEPOSIT_DETAIL_WEBCLIENT_URI}
			http-headers: ${WS_DEPOSIT_DETAIL_WEBCLIENT_HEADERS}
		deposit-images:
			connection-timeout: ${WS_DEPOSIT_IMAGES_WEBCLIENT_CONNECTION_TIMEOUT}
			read-timeout: ${WS_DEPOSIT_IMAGES_WEBCLIENT_READ_TIMEOUT}
			max-connections: ${WS_DEPOSIT_IMAGES_WEBCLIENT_MAX_CONNECTIONS}
			pending-acquire-max-count: ${WS_DEPOSIT_IMAGES_WEBCLIENT_PENDING_ACQUIRE_MAX_COUNT}
			uri: ${WS_DEPOSIT_IMAGES_WEBCLIENT_URI}
			http-headers: ${WS_DEPOSIT_IMAGES_WEBCLIENT_HEADERS}
		customer-payments:
			connection-timeout: ${WS_CUSTOMER_PAYMENTS_WEBCLIENT_CONNECTION_TIMEOUT}
			read-timeout: ${WS_CUSTOMER_PAYMENTS_WEBCLIENT_READ_TIMEOUT}
			max-connections: ${WS_CUSTOMER_PAYMENTS_WEBCLIENT_MAX_CONNECTIONS}
			pending-acquire-max-count: ${WS_CUSTOMER_PAYMENTS_WEBCLIENT_PENDING_ACQUIRE_MAX_COUNT}
			uri: ${WS_CUSTOMER_PAYMENTS_WEBCLIENT_URI}
			http-headers: ${WS_CUSTOMER_PAYMENTS_WEBCLIENT_HEADERS}
		account-transaction:
			connection-timeout: ${WS_ACCOUNT_TRANSACTION_WEBCLIENT_CONNECTION_TIMEOUT}
			read-timeout: ${WS_ACCOUNT_TRANSACTION_WEBCLIENT_READ_TIMEOUT}
			max-connections: ${WS_ACCOUNT_TRANSACTION_WEBCLIENT_MAX_CONNECTIONS}
			pending-acquire-max-count: ${WS_ACCOUNT_TRANSACTION_WEBCLIENT_PENDING_ACQUIRE_MAX_COUNT}
			uri: ${WS_ACCOUNT_TRANSACTION_WEBCLIENT_URI}
			http-headers: ${WS_ACCOUNT_TRANSACTION_WEBCLIENT_HEADERS}
	```

	## Convenciones
	- Claves en `kebab-case`.
	- Servicio reusable: cambiar únicamente placeholders de identidad del servicio.
	- El código Java debe consumir estas propiedades con `@Value` o `@ConfigurationProperties`.
	- Si el ejemplo histórico contiene `${VAR:default}`, convertirlo obligatoriamente a `${VAR}` al generar `application.yml`.
	- Generar además `applicationlocal.yaml` con valores literales de prueba local obtenidos desde Helm (preferencia `dev`) y sin placeholders.

	## Alcance
	Aplicable a:
	- `src/main/resources/application.yml`
	- `src/main/resources/applicationlocal.yaml`

	```
	````
```markdown
# PROMPT BASE — ADAPTERS OUTPUT CON WEBCLIENT (WEBFLUX)

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la implementación de adapters de salida en infraestructura que consumen servicios externos usando `WebClient` de Spring WebFlux.

## Reglas obligatorias
- Cada adapter debe implementar un puerto `*PortOutput` de `application.port.output`.
- Usar `org.springframework.web.reactive.function.client.WebClient` para la invocación externa.
- Mantener contrato reactivo con `reactor.core.publisher.Mono`.
- No incluir lógica de negocio en el adapter; solo integración, mapeo técnico y manejo de error técnico.
- Declarar adapter con `@Component` y usar inyección por constructor (ej: `@RequiredArgsConstructor`).
- Inyectar bean `WebClient` específico por servicio (ej: `customerPaymentsWebClient`), alineado a la configuración del paso 07.
- No usar logs con SLF4J (`@Slf4j`, `LoggerFactory`, `log.info/error/debug`).
- Anotar métodos de adapter con `@BpLogger`.
- Inyectar `private final CustomLogLevelHandler customLogLevelHandler;` para trazas técnicas.

## Ubicación y package
- Package base obligatorio: `com.pichincha.sp.infrastructure.output.adapter`.

## Convenciones de nombre
- Clase adapter: `<Servicio><Operacion>AdapterOutput`
- Debe implementar: `<Servicio><Operacion>PortOutput`
- Método principal: `execute(...)`

Nota de compatibilidad:
- Si el puerto ya existe con nombre de método específico (ej: `consultarCuentasActivas01(...)`), respetar la firma existente y no forzar renombre a `execute(...)`.

## Firma mínima esperada
- Entrada del método `execute`: DTO de dominio `*RequestDto`.
- Salida del método `execute`: `Mono<*ResponseDto>`.

## Reglas de invocación `WebClient`
- Construir request técnico con los `record` de `infrastructure.output.adapter.request`.
- Consumir endpoint con `WebClient` (`post/get` según levantamiento).
- Deserializar response técnico en `infrastructure.output.adapter.response.*`.
- Mapear response técnico hacia DTO de dominio de salida.
- Manejar errores técnicos con trazabilidad (status HTTP, timeout, parseo, conectividad).
- Usar flujo recomendado por operación: invocación (`executeRequest`) -> logging de respuesta (`doOnNext`) -> mapeo de error (`onErrorMap`) -> validación funcional/técnica de payload (`flatMap(validateResponse)`).
- En **todos** los métodos principales de adapters output, usar obligatoriamente la anotación `@EventAudit` con este formato:

```java
@EventAudit(service = "WSClientes0001", method = "ConsultarCuentasActivas01", type = AuditType.T)
```

Reglas de parametrización:
- `service`: nombre del servicio invocado según levantamiento (ej: `WSClientes0096`).
- `method`: nombre de la operación del servicio (ej: `CrearProspecto21`).
- `type`: fijo en `AuditType.T`.
- Para errores, usar el patrón obligatorio:

```java
.doOnError(error -> customLogLevelHandler.log(
	CustomLogLevel.ERROR,
	Thread.currentThread().getStackTrace(),
	error.getMessage() == null ? DomainValidationConstants.OPERATION_FLOW_ERROR : error.getMessage(),
	error.getMessage()))
```

## Reglas de dependencias e inyección
- Inyectar `WebClient` por constructor, referenciando bean específico por cliente.
- Si existen múltiples beans `WebClient`, usar `@Qualifier("<cliente>WebClient")`.
- No inyectar `WebClient.Builder` genérico en adapters output.
- No generar ni usar la clase `CommonHttpHeadersOperator` en adapters output.
- No hardcodear URL ni headers críticos en la clase; usar configuración externa.
- Mantener imports mínimos y compilables.
- No hardcodear códigos/mensajes de error configurables en el adapter; consumirlos desde configuración (`application.yml`) vía `@Value` o `@ConfigurationProperties`.
- Asegurar que esas propiedades estén externalizadas con `${...}` y respaldadas en Helm (`variables.own.config[].name`) por ambiente.

## Ejemplo de inyección esperada
```java
@Component
@RequiredArgsConstructor
public class CustomerPaymentsAdapterOutput implements CustomerPaymentsPortOutput {

	@Qualifier("customerPaymentsWebClient")
	private final WebClient customerPaymentsWebClient;
}
```

## Alcance
Aplicable a implementaciones en:
- `com.pichincha.sp.infrastructure.output.adapter`

```
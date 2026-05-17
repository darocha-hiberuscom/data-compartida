```markdown
# PROMPT BASE — CONFIGURACIÓN WEBCLIENT PARA OUTPUT ADAPTERS

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la configuración de `WebClient` para adapters de salida en arquitectura hexagonal/reactiva usando una clase base `WebClientConfig` y propiedades tipadas con `@ConfigurationProperties(prefix = "webclient")`.

## Reglas obligatorias
- Centralizar utilitarios comunes en una clase base `WebClientConfig`.
- Crear una clase `@Configuration` por cliente externo que extienda `WebClientConfig`.
- Exponer bean de `WebClient` por cliente (evitar `WebClient.Builder` como contrato principal en adapters).
- No generar ni usar clase auxiliar `CommonHttpHeadersOperator`.
- Resolver headers HTTP únicamente mediante `WebClientProperties` y aplicarlos en `WebClientConfig`.
- No hardcodear endpoints, timeouts ni credenciales en adapters.
- Obtener propiedades desde configuración externa (`application.yml`) mediante `WebClientProperties`.
- Mantener compatibilidad con Reactor (`Mono`/`Flux`) y adapters `PortOutput`.

## Patrón de implementación obligatorio
- Clase base común:
  - `infrastructure.output.adapter.config.WebClientConfig`
- Clase de propiedades:
  - `infrastructure.output.adapter.properties.webclient.WebClientProperties`
  - anotada con `@ConfigurationProperties(prefix = "webclient")`
- Configuración por cliente (ejemplo customer-payments):

```java
@Configuration
public class CustomerPaymentsWebClientConfig extends WebClientConfig {

    @Bean
    public WebClient customerPaymentsWebClient(final WebClientProperties webClientProperties) {
        return getWebClientBuilder(webClientProperties.customerPayments()).build();
    }
}
```

- Estructura mínima recomendada para `WebClientProperties`:

```java
@ConfigurationProperties(prefix = "webclient")
public record WebClientProperties(
        HttpClientProperties transactionJournal,
        HttpClientProperties depositDetail,
        HttpClientProperties depositImages,
        HttpClientProperties customerPayments,
        HttpClientProperties accountTransaction) {

    public record HttpClientProperties(
            int connectionTimeout,
            java.time.Duration readTimeout,
            int maxConnections,
            java.time.Duration maxIdleTime,
            int pendingAcquireMaxCount,
            java.util.List<String> httpHeaders,
            java.time.Duration pendingAcquireTimeout,
            String uri) {
    }
}
```

## Convenciones de configuración
- Prefijo obligatorio de propiedades: `webclient.<cliente>.*`.
- Ejemplo de claves por cliente:
  - `connection-timeout`
  - `read-timeout`
  - `max-connections`
  - `max-idle-time`
  - `pending-acquire-max-count`
  - `pending-acquire-timeout`
  - `http-headers`
  - `uri`
- Si aplica seguridad, parametrizar headers y tokens por propiedades.
- No usar operadores auxiliares externos para headers en adapters.

## Reglas de timeout y resiliencia técnica
- Configurar timeout de conexión y lectura.
- Mapear errores técnicos HTTP/IO/timeout a excepciones técnicas trazables.
- Mantener separación: resiliencia técnica en infraestructura, no en dominio.

## Estructura recomendada
- Clase base:
  - `...infrastructure.output.adapter.config.WebClientConfig`
- Config por cliente:
  - `...infrastructure.output.adapter.<cliente>.config.*WebClientConfig`
- Properties:
  - `...infrastructure.output.adapter.properties.webclient.WebClientProperties`

## Alcance
Aplicable a configuración de clientes para adapters en:
- `...infrastructure.output.adapter`

```
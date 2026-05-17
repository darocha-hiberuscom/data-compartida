````markdown
# IMPLEMENTACIÓN 07 — CONFIGURACIÓN DE WEBCLIENT PARA OUTPUT ADAPTERS

> Nota: este archivo corresponde a un paso ejecutable de implementación.
> Orden oficial de ejecución: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## AGENTE REQUERIDO
- `agente_implementador_java`

## PREREQUISITO
- Paso 03 completado (puertos de salida `PortOutput` generados).
- Paso 06 completado (adapters output implementados con `WebClient`).
- Archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` actualizado.
- Sección 4.7 disponible para endpoints de servicios invocados.

## OBJETIVO
Configurar el wiring técnico de `WebClient` para que los adapters de salida consuman servicios externos con URLs y timeouts externalizados usando el patrón: clases por cliente que extienden `WebClientConfig` y propiedades tipadas en `WebClientProperties` (`@ConfigurationProperties(prefix = "webclient")`).

## ALCANCE
- Crear/actualizar configuración en:
  - `repos/destino/${nombre_proyecto}/src/main/java/.../infrastructure/output/adapter/<cliente>/config`
- Crear/actualizar clase base de configuración en:
  - `repos/destino/${nombre_proyecto}/src/main/java/.../infrastructure/output/adapter/config/WebClientConfig.java`
- Crear/actualizar clases de propiedades en:
  - `repos/destino/${nombre_proyecto}/src/main/java/.../infrastructure/output/adapter/properties/webclient/WebClientProperties.java`
- Crear/actualizar propiedades en:
  - `repos/destino/${nombre_proyecto}/src/main/resources/application.yml`
- Dejar adapters del paso 06 consumiendo `WebClient` inyectado por configuración.

## INSTRUCCIONES

### 1) Fuente de verdad
Usar como fuente principal:
- `./migracion/LEVANTAMIENTO_INFORMACION.md`

Prioridad de lectura:
1. Sección 4.7 — Endpoints y tipo de invocación por servicio.
2. Sección 12 — Resumen global de servicios invocados.
3. Sección 13 — Contrato de request/response por servicio.

Si faltan datos técnicos:
- no inventar,
- registrar `FALTA DETALLE` con pista trazable.

### 2) Configuración de beans WebClient
Crear configuración por cliente que extienda `WebClientConfig` y exponga beans de `WebClient` para uso de adapters output.

Reglas obligatorias:
- Inyección por constructor en adapters (no instanciar `new WebClient` dentro del adapter).
- Usar `getWebClientBuilder(webClientProperties.<cliente>()).build()` en cada bean.
- Mantener naming consistente por cliente (ej: `customerPaymentsWebClient`).
- Gestionar headers únicamente desde `WebClientProperties.HttpClientProperties.httpHeaders` aplicados en `WebClientConfig`.
- No crear ni usar operadores auxiliares de headers (incluyendo `CommonHttpHeadersOperator`).
- Ejemplo obligatorio de patrón:

```java
@Configuration
public class CustomerPaymentsWebClientConfig extends WebClientConfig {

    @Bean
    public WebClient customerPaymentsWebClient(final WebClientProperties webClientProperties) {
        return getWebClientBuilder(webClientProperties.customerPayments()).build();
    }
}
```

### 3) Propiedades externas
Definir propiedades por servicio invocado en `application.yml` con prefijo obligatorio `webclient` y mapearlas a `WebClientProperties`.

Estructura requerida:
- Clase `WebClientProperties` anotada con `@ConfigurationProperties(prefix = "webclient")`.
- Record principal con clientes:
  - `transactionJournal`
  - `depositDetail`
  - `depositImages`
  - `customerPayments`
  - `accountTransaction`
- Record interno `HttpClientProperties` con campos:
  - `connectionTimeout`
  - `readTimeout`
  - `maxConnections`
  - `maxIdleTime`
  - `pendingAcquireMaxCount`
  - `httpHeaders`
  - `pendingAcquireTimeout`
  - `uri`

Reglas:
- No hardcodear valores sensibles en código.
- Mantener valores por ambiente fuera de clases Java.
- No resolver headers en adapters con clases utilitarias externas; resolverlos en la construcción del `WebClient`.

### 4) Integración con adapters
Ajustar adapters del paso 06 para usar el bean de `WebClient` configurado.

Reglas:
- No alterar contratos `PortOutput`.
- Preservar retorno reactivo `Mono<...>`.
- Conservar mapeo request/response ya definido en pasos previos.

### 5) Estándar técnico compartido
Aplicar exactamente las reglas del prompt compartido:
- `.github/prompts/shared/PROMPT_BASE_WEBCLIENT_OUTPUT_CONFIG.md`

## SALIDA OBLIGATORIA
- Configuración de `WebClient` funcional para todos los adapters output.
- Propiedades externas de endpoints y timeouts por servicio.
- Adapters output conectados a los beans configurados.

## CHECKLIST DE VALIDACIÓN
- [ ] Existe clase base `WebClientConfig`
- [ ] Existe clase `WebClientProperties` con `@ConfigurationProperties(prefix = "webclient")`
- [ ] Existen clases `*WebClientConfig` por cliente extendiendo `WebClientConfig`
- [ ] Endpoints externalizados por servicio
- [ ] Timeouts de conexión y lectura configurados
- [ ] Headers configurados vía `WebClientProperties` y aplicados en `WebClientConfig`
- [ ] Adapters no hardcodean URLs ni instancia de cliente
- [ ] Adapters usan inyección de `WebClient`
- [ ] Contrato reactivo `Mono<...>` preservado
- [ ] Compilación sin errores de imports/configuración

## REGLAS
- NO modificar lógica del negocio existente.
- NO inventar endpoints no evidenciados.
- Aplicar reglas de:
  - `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`
  - `.github/prompts/shared/PROMPT_BASE_WEBCLIENT_OUTPUT_CONFIG.md`

````
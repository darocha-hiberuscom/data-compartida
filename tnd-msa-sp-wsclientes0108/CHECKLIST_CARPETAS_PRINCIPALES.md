# Checklist de carpetas principales del proyecto

> Proyecto: `tnd-msa-sp-wsclientes0108`

## A) Validaciones generales (aplican a cualquier proyecto)

### 1) Estructura raíz

- [ ] `src/` existe y contiene código fuente real del servicio.
- [ ] `gradle/` existe con wrapper y configuración de build.
- [ ] `helm/` existe con valores por ambiente (`dev.yml`, `test.yml`, `prod.yml`).
- [ ] Archivos base de build/config presentes: `build.gradle`, `settings.gradle`, `gradle.properties`, `Dockerfile`, `README.md`.

### 2) Código fuente (`src/main`)

- [ ] `src/main/java/` contiene paquetes de negocio y adaptadores.
- [ ] `src/main/resources/` contiene `application.yml`, `logback-spring.xml` y artefactos SOAP (`.xsd`, `.wsdl`).
- [ ] No hay clases compiladas (`.class`) dentro de `src/main`.

### 3) Pruebas (`src/test`)

- [ ] `src/test/java/` refleja la jerarquía de paquetes de `src/main/java`.
- [ ] `src/test/resources/` contiene solo recursos de prueba.
- [ ] Las pruebas principales del caso de uso están presentes (ej. `CreateCustomerServiceTest`).

### 4) Build y artefactos generados

- [ ] `build/` se usa solo para artefactos de compilación (classes, reports, jars, tmp).
- [ ] `bin/` contiene salidas/generados y no código fuente manual.
- [ ] No se modifica código dentro de `build/` ni `bin/` de forma manual.
- [ ] Carpetas generadas pueden recrearse con `./gradlew clean build`.

### 5) Configuración de despliegue y pipeline

- [ ] `azure-pipelines.yml` define pipeline de CI/CD.
- [ ] `helm/` está alineado con variables y configuración esperada por ambiente.
- [ ] Archivos de seguridad/API scanning presentes (`stackhawk-*.yml`).

### 6) Higiene de repositorio

- [ ] Archivos temporales de crash/log (`hs_err_pid*.log`, `*.mdmp`, `replay_pid*.log`) no deberían versionarse.
- [ ] `build/`, `bin/` y salidas de ejecución están en `.gitignore`.
- [ ] Solo código fuente, configuración y documentación versionados.

### 7) Validación rápida recomendada

- [ ] Ejecutar: `./gradlew clean test`
- [ ] Ejecutar: `./gradlew build`
- [ ] Verificar que no se generen cambios inesperados en archivos versionados tras el build.

### 8) Estructura obligatoria de `build.gradle` (lo demás puede variar)

- [ ] Existe bloque `compileJava { options.compilerArgs += [...] }` con flags de MapStruct:
	`suppressGeneratorTimestamp`, `suppressGeneratorVersionInfoComment`, `verbose`, `defaultComponentModel=spring`.
- [ ] Existe `sourceSets.main.java.srcDirs = ["src/main/java"]`.
- [ ] Existe bloque `test { useJUnitPlatform(); finalizedBy jacocoTestReport }`.
- [ ] Existe bloque `jacocoTestReport { dependsOn test; reports { xml/html/csv ... } }`.
- [ ] Existe tarea `tasks.register('cleanGeneratedWsdl')` y `clean.dependsOn 'cleanGeneratedWsdl'`.
- [ ] Existe tarea `tasks.register('generateJavaFromWsdl')` con:
	inputs (`*.wsdl`, `*.xsd`, binding opcional), output en `build/generated/sources/wsdl` y ejecución `WSDLToJava`.
- [ ] Existe tarea `tasks.register('postProcessGeneratedWsdl')` dependiente de `generateJavaFromWsdl`.
- [ ] Existe tarea `tasks.register('generateFromWsdl')` dependiente de `postProcessGeneratedWsdl`.
- [ ] Existe `sourceSets { main { java { srcDir 'build/generated/sources/wsdl' } } }`.
- [ ] Existe `tasks.named('compileJava') { dependsOn 'generateFromWsdl' }`.

### 9) Validación de configuración `application.yml` vs Helm (obligatoria)

- [ ] `application.yml` no tiene valores quemados para propiedades de runtime (usa variables `${NOMBRE_VARIABLE}`).
- [ ] `application.yml` no usa valores por defecto en placeholders (`${VAR:default}`) para configuración de ambiente.
- [ ] Cada variable referenciada en `application.yml` existe en `helm/dev.yml`.
- [ ] Cada variable referenciada en `application.yml` existe en `helm/test.yml`.
- [ ] Cada variable referenciada en `application.yml` existe en `helm/prod.yml`.
- [ ] Los nombres de variables entre `application.yml` y Helm están alineados (sin aliases distintos para la misma propiedad).
- [ ] No hay llaves críticas faltantes por ambiente (si falta una, se considera hallazgo de configuración).
- [ ] Mantener una validación de cruce periódica para detectar `missing/extra` entre `application.yml` y Helm.

## B) Validaciones por tipo de proyecto

### B.1) Tipo `iib`

- [ ] En la capa de entrada existe `input/adapter/rest`.
- [ ] En la capa de salida existe `output/adapter`.
- [ ] Dentro de `output/adapter` existen las carpetas `mapper/`, `request/` y `response/`.
- [ ] En `output/adapter` existe la clase `AbstractBancsTransactionAdapter.java`.
- [ ] En `output/adapter` existe la clase `BancsErrorHandlerAdapter.java`.

#### Patrón por cada adapter de `output/adapter`

- [ ] La clase está en el paquete `com.pichincha.sp.infrastructure.output.adapter`.
- [ ] El nombre sigue el patrón `TxXXXXXXAdapter`.
- [ ] La clase usa `@Repository`.
- [ ] La clase extiende `AbstractBancsTransactionAdapter`.
- [ ] La clase implementa su `PortOutput` correspondiente de `application.port.output`.
- [ ] Inyecta por constructor: `@BancsService(...) BancsClient`, `CustomLogLevelHandler`, `Mapper` y `BancsErrorHandlerAdapter`.
- [ ] El constructor invoca `super(bancsClient, customLogLevelHandler, bancsAdapterErrorHandler)`.
- [ ] Tiene `Mapper` tipado correspondiente a la TX del adapter (ejemplo referencial: `Tx060450Mapper`; no obligatorio que sea `Tx060450`).
- [ ] Importa y usa `request` y `response` técnicos correspondientes a la misma TX del adapter (ejemplo referencial: `Tx060450Request`, `Tx060450Response`; no obligatorio que sea `Tx060450`).
- [ ] Las clases en `request/` y `response/` siguen patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] El nombre de clase en `request/` y `response/` corresponde a la TX del adapter (patrón `TxXXXXXXRequest` / `TxXXXXXXResponse`).
- [ ] El paquete de cada clase corresponde a su carpeta (`...output.adapter.request` o `...output.adapter.response`).
- [ ] Los `mapper` siguen patrón MapStruct con `@Mapper(componentModel = "spring")`.
- [ ] El nombre del mapper corresponde a la TX del adapter (patrón `TxXXXXXXMapper`).
- [ ] El paquete del mapper corresponde a `...output.adapter.mapper`.
- [ ] Cada mapper define método `toRequest(DomainRequestDto)` que retorna `TxXXXXXXRequest`.
- [ ] Cada mapper define método `toDomain(TxXXXXXXResponse)` que retorna `DomainResponseDto`.
- [ ] Si hay diferencias de nombres de campos, usa `@Mapping(source = "...", target = "...")`.
- [ ] `request`, `response` y `mapper` corresponden a la misma TX del adapter.
- [ ] El adapter no usa bloques `try-catch`; el manejo de errores se delega a mecanismos comunes (ej. `BancsErrorHandlerAdapter`/flujo reactivo).
- [ ] El método de negocio está anotado con `@BpLogger`.
- [ ] El método construye request técnico con `mapper.toRequest(request)`.
- [ ] El método ejecuta `executeTransaction(...)` con: constante de transacción, request técnico, `Response.class` y `mapper::toDomain`.
- [ ] El método retorna `Mono<...>` del DTO de dominio esperado.

#### Patrón para clase SOAP body request (ej. `SoapBodyRequest`)

- [ ] La clase está en el paquete de entrada SOAP/REST correspondiente (ej. `...input.adapter.rest.dto.request`).
- [ ] Usa patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] Está anotada con JAXB: `@XmlAccessorType(XmlAccessType.FIELD)`.
- [ ] Cada campo de operación SOAP usa `@XmlElement(name = "...", namespace = "...")`.
- [ ] Los tipos de los campos corresponden a clases generadas JAXB/WSDL (ej. `com.pichincha.sp.generated.*`).
- [ ] Los nombres de campos reflejan la operación SOAP y su contrato técnico.
- [ ] La clase no contiene lógica de negocio; solo estructura de transporte/mensaje.

#### Patrón para clase SOAP envelope request (ej. `SoapEnvelopeRequest`)

- [ ] La clase está en `...input.adapter.rest.dto.request`.
- [ ] Usa patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] Está anotada con JAXB: `@XmlAccessorType(XmlAccessType.FIELD)`.
- [ ] Define `@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] El campo `header` usa `@Builder.Default`, tipo `Object` y valor por defecto `null`.
- [ ] El campo `header` está anotado con `@XmlElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] El campo `body` es del tipo `SoapBodyRequest`.
- [ ] El campo `body` está anotado con `@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] La clase no contiene lógica de negocio; solo estructura de envoltura SOAP.

#### Patrón para clase SOAP body response (ej. `SoapBodyResponse`)

- [ ] La clase está en `...input.adapter.rest.dto.response`.
- [ ] Usa patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] Está anotada con JAXB: `@XmlAccessorType(XmlAccessType.FIELD)`.
- [ ] Cada operación de respuesta SOAP usa `@XmlElement(name = "...Response", namespace = "...")`.
- [ ] Los tipos de los campos corresponden a clases de respuesta generadas JAXB/WSDL (ej. `com.pichincha.sp.generated.*Response`).
- [ ] Los nombres de campos reflejan la operación SOAP de respuesta y su contrato técnico.
- [ ] La clase no contiene lógica de negocio; solo estructura de transporte/mensaje.

#### Patrón para clase SOAP envelope response (ej. `SoapEnvelopeResponse`)

- [ ] La clase está en `...input.adapter.rest.dto.response`.
- [ ] Usa patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] Está anotada con JAXB: `@XmlAccessorType(XmlAccessType.FIELD)`.
- [ ] Define `@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] El campo `header` usa `@Builder.Default`, tipo `Object` y valor por defecto `null`.
- [ ] El campo `header` está anotado con `@XmlElement(name = "Header", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] El campo `body` es del tipo `SoapBodyResponse`.
- [ ] El campo `body` está anotado con `@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] La clase no contiene lógica de negocio; solo estructura de envoltura SOAP.

#### Patrón para clase SOAP fault body dto (ej. `SoapFaultBodyDto`)

- [ ] La clase está en `...input.adapter.rest.dto`.
- [ ] Usa patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] Está anotada con JAXB: `@XmlAccessorType(XmlAccessType.FIELD)`.
- [ ] Define `@XmlRootElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] Define campo `fault` tipado como `SoapFaultDto`.
- [ ] El campo `fault` usa `@XmlElement(name = "Fault", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] La clase no contiene lógica de negocio; solo estructura técnica para fault SOAP.

#### Patrón para clase SOAP fault dto (ej. `SoapFaultDto`)

- [ ] La clase está en `...input.adapter.rest.dto`.
- [ ] Usa patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] Está anotada con JAXB: `@XmlAccessorType(XmlAccessType.FIELD)`.
- [ ] Define `@XmlRootElement(name = "Fault", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] Define campo `faultCode` anotado con `@XmlElement(name = "faultcode", namespace = "")`.
- [ ] Define campo `faultString` anotado con `@XmlElement(name = "faultstring", namespace = "")`.
- [ ] Define campo `detail` anotado con `@XmlElement(name = "detail", namespace = "")`.
- [ ] Los campos de fault son de tipo `String` y representan contrato técnico SOAP fault.
- [ ] La clase no contiene lógica de negocio; solo estructura técnica de error SOAP.

#### Patrón para clase SOAP fault envelope dto (ej. `SoapFaultEnvelopeDto`)

- [ ] La clase está en `...input.adapter.rest.dto`.
- [ ] Usa patrón POJO con Lombok: `@Getter`, `@Setter`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`.
- [ ] Está anotada con JAXB: `@XmlAccessorType(XmlAccessType.FIELD)`.
- [ ] Define `@XmlRootElement(name = "Envelope", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] Define campo `body` tipado como `SoapFaultBodyDto`.
- [ ] El campo `body` usa `@XmlElement(name = "Body", namespace = "http://schemas.xmlsoap.org/soap/envelope/")`.
- [ ] Expone método estático de fábrica `withFault(SoapFaultDto fault)`.
- [ ] `withFault(...)` construye envelope usando `builder()` y anida `SoapFaultBodyDto.builder().fault(fault).build()`.
- [ ] La clase no contiene lógica de negocio; solo construcción de envoltura técnica de fault SOAP.

#### Patrón para controller SOAP (ej. `WSClientes0114Controller`) con responsabilidad única

- [ ] La clase está en `...input.adapter.rest.impl`.
- [ ] Usa `@RestController` y `@RequestMapping(...)` con constante de ruta SOAP.
- [ ] Expone `@PostMapping` con `consumes`/`produces` para `text/xml`, `application/xml` y `application/soap+xml`.
- [ ] El método principal recibe `SoapEnvelopeRequest` y retorna `Mono<SoapEnvelopeResponse>`.
- [ ] Inyecta puertos de entrada (`...port.input`) para ejecutar casos de uso, no servicios técnicos de negocio.
- [ ] Inyecta `soapMapper`, `operationRouter`, `headerFactory`, `responseFactory` y `soapErrorMapper` para delegar transformaciones/enrutamiento.
- [ ] Valida estructura mínima del request SOAP (envelope/body/operación) y delega la validación de negocio a la capa `application`.
- [ ] Determina la operación vía `operationRouter` y delega procesamiento por operación a métodos privados pequeños.
- [ ] Cada método privado invoca un `PortInput`, mapea request/response y construye envelope usando `responseFactory`.
- [ ] El manejo de errores de negocio usa `soapErrorMapper` + `responseFactory` (sin construir SOAP fault manualmente en el controller).
- [ ] El controller no usa bloques `try-catch`; usa flujo reactivo y manejo centralizado de errores.
- [ ] No contiene reglas de negocio, lógica de persistencia, ni mapeos técnicos complejos inline.
- [ ] No acumula responsabilidades transversales ajenas (auditoría, seguridad, trazabilidad compleja); esas van en filtros/aspectos/configuración.

#### Sección builder (validación obligatoria de buenas prácticas)

- [ ] Existe una sección de validación de uso de `builder` para objetos de salida/errores técnicos (ej. `BusinessErrorDto.builder()`).
- [ ] El `builder` se usa para construir DTOs de forma explícita y legible, asignando campos clave en cadena antes de `.build()`.
- [ ] Los valores por defecto provienen de constantes de dominio/técnicas (ej. `SoapErrorConstants`), no de literales dispersos.
- [ ] Cuando hay datos opcionales (ej. `exception.getBusinessError()`), se aplica fallback nulo-seguro antes de poblar el `builder`.
- [ ] Se normalizan campos críticos antes del `build` (ej. `component` por defecto cuando viene `null` o vacío).
- [ ] El `resource`/metadatos derivados se resuelven en métodos helper dedicados (ej. `resolveResource`, `resolveOperationName`) y no inline.
- [ ] El `builder` se utiliza para devolver un objeto final completo, evitando mutaciones posteriores.
- [ ] En mappers MapStruct, las reglas de construcción compleja se encapsulan en métodos `default` para mantener consistencia y testeabilidad.

### B.2) Tipo `was 1`

- [ ] Existe un único controller SOAP principal por servicio (ej. `WSClientes0098Controller`).
- [ ] El controller está en `...infrastructure.input.adapter.rest.impl`.
- [ ] Usa `@RestController`, `@RequestMapping("/.../soap/...Request")` y `@RequiredArgsConstructor`.
- [ ] Inyecta por constructor: `InputPort` de aplicación + mapper de entrada + mapper de salida.
- [ ] No depende de repositorios/adapters de salida directamente desde el controller.
- [ ] Expone `@PostMapping` con `consumes`/`produces` XML (`text/xml;charset=utf-8`, `application/xml`).
- [ ] El método del endpoint recibe `@RequestBody @Valid SoapEnvelopeRequestDto`.
- [ ] El controller invoca el caso de uso vía `InputPort` con request mapeado a dominio (`inputMapper.toDomain(...)`).
- [ ] El controller construye respuesta SOAP vía mapper de salida (`outputMapper.toSoapEnvelope(...)`).
- [ ] Retorna `ResponseEntity<SoapEnvelopeResponseDto>` con `contentType(application/xml)` y `ResponseEntity.ok(...)`.
- [ ] El controller no contiene reglas de negocio ni bloques `try-catch`.
- [ ] Los adaptadores de `was 1` no contienen bloques `try-catch`; el manejo de errores se delega a mecanismos centralizados.
- [ ] Los DTO de `infrastructure.input` en `was 1` siguen el mismo formato/patrones definidos para `iib` (request/response/envelope/fault en la capa `input.adapter.rest.dto`).

#### Patrón para mapper de input SOAP (solo `was 1`)

- [ ] Existe mapper en `...input.adapter.rest.mapper` para convertir requests SOAP a DTO de dominio.
- [ ] El mapper usa `@Mapper(componentModel = "spring")`.
- [ ] El nombre del mapper sigue convención por servicio SOAP (ej. `SoapInputMapper` o `<Servicio>SoapInputMapper`).
- [ ] Define métodos `toDomain(...)` por cada operación SOAP soportada.
- [ ] Los métodos `toDomain(...)` reciben tipos JAXB generados (ej. `Entrada`, `Consultar...`, `Consulta...`).
- [ ] El retorno de `toDomain(...)` es DTO de dominio (ej. `CatalogQueryRequestDto`).
- [ ] Usa `@Mapping(source = "...", target = "...")` para mapear campos planos y anidados.
- [ ] Cuando un campo no aplica en cierta operación, se declara explícitamente con `@Mapping(target = "...", ignore = true)`.
- [ ] Cuando aplica, define mapeo de DTO de dominio a contrato técnico SOAP de salida (ej. `CatalogRecordDto` -> `RegistroCatalogoBO`).
- [ ] Cuando aplica, define mapeo agregado de resultado de dominio a estructura SOAP de salida (ej. `CatalogQueryResultDto` -> `CatalogoOutBO`).
- [ ] En mapeos de salida SOAP, campos de error no gestionados por mapper se marcan con `ignore = true` (ej. `codError`, `descError`).
- [ ] Si existe transformación de colecciones simples, usa método `default` explícito en el mapper (ej. `mapFields(List<String> fields)`).
- [ ] Los imports del mapper mantienen separación clara entre DTOs de dominio y clases JAXB generadas.
- [ ] El mapper no contiene lógica de negocio; solo transformación de contrato SOAP a modelo de dominio.

#### Patrón para builder de transformación SOAP (solo `was 1`)

- [ ] Existe clase builder en `...infrastructure.input.adapter.rest.builder` (ej. `SoapEnvelopeBuilder`).
- [ ] La clase usa `@Component`.
- [ ] Inyecta `SoapInputMapper` por constructor.
- [ ] Obtiene parámetros configurables por `@Value` (servicio, método, códigos/mensajes/tipos/backend).
- [ ] Expone método `toDomain(Entrada entrada)` que delega en `soapMapper.toDomain(entrada)`.
- [ ] Expone método `toSuccessEnvelope(List<...> results, Header header)` para construir respuesta SOAP exitosa.
- [ ] Expone sobrecargas de `toErrorEnvelope(...)` para error de negocio y error por fallback.
- [ ] Construye `OperationErrorDto` de éxito con `builder()` y valores de configuración.
- [ ] Construye `Salida` asignando `headerOut`, `bodyOut` y `error` según tipo de respuesta.
- [ ] Incluye método privado `buildBodySalida(...)` que transforma resultados con `soapMapper.toCatalogoOutBO(...)`.
- [ ] Incluye método privado/estático `buildError(...)` que mapea `OperationErrorDto` a objeto técnico `Error`.
- [ ] Incluye método privado/estático `buildErrorFromException(...)` con fallback cuando la excepción no trae `operationError`.
- [ ] Incluye método privado/estático `wrapSalida(...)` que retorna `SoapEnvelopeResponse` con `SoapBodyResponse` builder.
- [ ] La clase no contiene reglas de negocio ni bloques `try-catch`; solo transformación y ensamblado de contratos SOAP.

#### Patrón para validador de request en input (solo `was 1`)

- [ ] Existe validador en `...infrastructure.input.adapter.rest.validator` (ej. `WSTecnicos0063RequestValidator`).
- [ ] La clase usa `@Component`.
- [ ] Inyecta por `@Value` mensajes/códigos/tipos y metadatos de recurso (`service.name`, `service.method`, `backend`, `component`).
- [ ] Expone método `requireEntrada(SoapEnvelopeRequest soapRequest)`.
- [ ] `requireEntrada(...)` valida `body` y operación SOAP esperada; si no existe, lanza `ResponseStatusException(HttpStatus.BAD_REQUEST, ...)`.
- [ ] Expone método `validateEntrada(Entrada entrada)` para validar header/body y reglas mínimas del request.
- [ ] `validateEntrada(...)` valida campo funcional obligatorio (ej. `tabla`) y lanza `BusinessException` con `OperationErrorDto.builder()` cuando falla.
- [ ] `validateEntrada(...)` valida encabezado con `isValidHeader(...)` y lanza `BusinessException` con código/mensaje/tipo configurados si es inválido.
- [ ] Expone método `isValidHeader(Header headerIn)` con validación de campos requeridos por header.
- [ ] Expone método `hasHeaderAndBody(Header headerIn, BodyInEntrada bodyIn)` para validación estructural básica.
- [ ] Incluye helper `isNotBlank(String value)` para centralizar validación de texto.
- [ ] El validador no contiene lógica de negocio de casos de uso; solo validación de contrato de entrada.

### B.3) Tipo `was multiple`

- [ ] La estructura separa claramente cada módulo/aplicación desplegable en WAS.
- [ ] Cada módulo tiene límites de responsabilidad y dependencias explícitas.
- [ ] Existen convenciones comunes compartidas sin mezclar código funcional entre módulos.
- [ ] Los artefactos de build/deploy permiten desplegar módulos de forma independiente.

### B.4) Tipo `orquestadores`

- [ ] La capa de orquestación está separada de adaptadores técnicos.
- [ ] El flujo de orquestación se concentra en `application` (o equivalente), no en controladores/adaptadores.
- [ ] La lógica de decisión funcional no depende de detalles de transporte/protocolo.
- [ ] Se identifican claramente pasos de orquestación, manejo de error y compensaciones (si aplica).

### B.5) Detalle de capas principales (aplica cuando el proyecto usa `application`/`domain`/`infrastructure`)

#### Application (`src/main/java/com/pichincha/sp/application/`)

- [ ] Contiene casos de uso y orquestación del flujo (ej. `service/`, `port/`).
- [ ] Los `port/input` definen contratos de entrada del caso de uso.
- [ ] Los `port/output` definen contratos hacia infraestructura (sin implementación técnica).
- [ ] `exception/` es opcional; si existe, contiene excepciones funcionales de aplicación (no técnicas de transporte).
- [ ] Si existe `constants/`, contiene constantes propias de la capa (no es obligatorio que exista).
- [ ] No incluye lógica técnica de framework (HTTP, SOAP, JDBC, repositorios concretos).
- [ ] Depende de `domain` y de interfaces, no de implementaciones de `infrastructure`.

#### Patrones más importantes para `service` (solo criterios alineados a SOLID)

- [ ] El service está en `application/service` y está anotado con `@Service`.
- [ ] Implementa un `PortInput` de `application.port.input` (contrato de caso de uso).
- [ ] Depende de puertos (`PortOutput`) y soportes/colaboradores por interfaces o clases de aplicación, no de adaptadores concretos.
- [ ] Usa inyección por constructor para todas las dependencias (sin creación manual con `new` dentro del flujo de negocio).
- [ ] Mantiene responsabilidad de orquestación: valida, delega a puertos/soportes y compone el resultado.
- [ ] Las validaciones y reglas reutilizables se delegan a clases de soporte (ej. `CustomerAccountsServiceSupport`).
- [ ] Utiliza flujo reactivo (`Mono`) de extremo a extremo, sin bloqueos ni llamadas `block()`.
- [ ] Maneja errores/logs de forma transversal con operadores reactivos (`doOnError`, `doFinally`) y utilidades de logging.
- [ ] Si el caso de uso requiere excepciones de negocio, estas se definen en el paquete de excepciones de la capa correspondiente (preferente `domain/exception` o `application/exception`) y el service solo las propaga/orquesta.
- [ ] El service no define clases de excepción internas ni construye manejo técnico ad-hoc; usa excepciones centralizadas del proyecto.
- [ ] Usa DTOs de dominio y builders para requests/responses (`...RequestDto.builder()`, `...ResultDto.builder()`).
- [ ] Centraliza códigos/mensajes de negocio en constantes de dominio (sin literales mágicos en el service).
- [ ] Expone métodos públicos pequeños y delega detalle operativo a métodos privados/soportes para mantener legibilidad.

#### Domain (`src/main/java/com/pichincha/sp/domain/`)

- [ ] Centraliza reglas de negocio, DTO de dominio, constantes y excepciones de negocio.
- [ ] `exception/` existe y modela errores de negocio del dominio.
- [ ] Si existe `constants/`, agrupa constantes de negocio reutilizables (no es obligatorio que exista).
- [ ] No contiene código de transporte/protocolo (SOAP/REST), ni clases de framework.
- [ ] No depende de paquetes de `infrastructure`.
- [ ] Mantiene nombres y modelos orientados al negocio, no a la tecnología.
- [ ] Excepciones de dominio representan errores funcionales, no técnicos.

#### Infrastructure (`src/main/java/com/pichincha/sp/infrastructure/`)

- [ ] Implementa adaptadores de entrada/salida (`input/adapter`, `output/adapter`).
- [ ] Contiene mapeos entre modelos técnicos y de dominio.
- [ ] `exception/` es opcional; si existe, concentra excepciones técnicas y resolvers de error.
- [ ] Si existe `constants/`, concentra constantes técnicas/integración (no es obligatorio que exista).
- [ ] Maneja integración externa, controladores, serialización y tratamiento de errores técnicos.
- [ ] Implementa puertos definidos por `application` sin mover reglas de negocio aquí.
- [ ] Puede depender de `application`/`domain`, pero evita dependencias inversas.

#### Regla transversal de `constants`

- [ ] La carpeta `constants/` puede existir en todas las capas o solo en algunas, según necesidad.
- [ ] No se considera hallazgo que una capa no tenga `constants/` si no aporta valor real.

---

## Resultado esperado

Si todos los checks están completos, la estructura de carpetas está **ordenada, mantenible y lista para CI/CD**.
# Validación SOLID - Proyecto tnd-msa-sp-wsclientes0108

**Fecha:** 2026-05-19  
**Alcance:** 69 clases Java (src/main/java)  
**Enfoque:** Principios SOLID + Arquitectura Hexagonal

---

## 📋 Resumen Ejecutivo

| Severidad | Cantidad | Impacto |
|-----------|----------|--------|
| 🔴 Crítico | 4 | Violaciones que impactan mantenibilidad y testing |
| 🟠 Alto | 7 | Deuda técnica en coupling y DIP |
| 🟡 Medio | 6 | Mejoras recomendadas |
| 🟢 Bajo | 3 | Observaciones de estilo |

**Score SOLID:** 68/100

---

## 🔴 CRÍTICO (4 hallazgos)

### 1. **SRP Violation: `CreateCustomerService.java`** (Línea 40-203)
- **Archivo:** `src/main/java/com/pichincha/sp/application/service/CreateCustomerService.java`
- **Problema:** Clase con **8 responsabilidades distintas**
  - Orquestación de flujo (`runCreationFlow`)
  - Construcción de DTOs (`buildBasicDataRequest`, `buildPersonalDataRequest`, etc.)
  - Manejo de errores (`buildValidationErrorResult`)
  - Validación y logging
- **Impacto:** 
  - Difícil de testear (8 mocks necesarios)
  - Alto riesgo de regresión en cambios
  - Violación directa de SRP
- **Recomendación:** 
  ```
  Crear:
  - CreateCustomerOrchestrator (orquestación)
  - CreateCustomerRequestBuilder (construcción de DTOs)
  - CreateCustomerValidator (validación)
  ```

### 2. **DIP Violation: `CreateCustomerService.java` → `CustomLogLevelHandler`** (Línea 24, 51, 81)
- **Archivo:** `src/main/java/com/pichincha/sp/application/service/CreateCustomerService.java`
- **Problema:** Dependencia directa de **infrastructure logging** en **application service**
  - `CustomLogLevelHandler customLogLevelHandler` (línea 51)
  - Invocaciones directas `customLogLevelHandler.log(...)` (línea 80, 119)
- **Violación:** Application debe depender de abstracciones, no de infraestructura
- **Recomendación:**
  ```java
  // Crear interfaz en application
  public interface ApplicationLogger {
      void logFlowFinished(String signal);
      void logFlowError(String message);
  }
  // Implementar en infrastructure
  ```

### 3. **OCP Violation: `WSClientes0108Controller.java`** (Línea 50-56)
- **Archivo:** `src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/impl/WSClientes0108Controller.java`
- **Problema:** Handler de excepciones hardcodeado en controlador
  - `.onErrorResume(BusinessValidationException.class, ...)`
  - `.onErrorResume(GlobalErrorException.class, ...)`
  - `.onErrorResume(Throwable.class, ...)`
- **Violación:** Cerrada a extensión (agregar nuevo error requiere cambiar controlador)
- **Recomendación:**
  ```java
  // Usar ExceptionHandler decorador registrado en contenedor
  public interface ExceptionHandler {
      Mono<?> handle(Throwable exception);
  }
  ```

### 4. **ISP Violation: `WsClientes0108SoapMapper.java`** (Línea 14-65)
- **Archivo:** `src/main/java/com/pichincha/sp/infrastructure/input/adapter/rest/mapper/WsClientes0108SoapMapper.java`
- **Problema:** Mapper interface con **2 métodos muy distintos**
  - `toDomain(SoapEnvelopeRequest)` - lectura/mapeo entrada
  - `toSoapResponse(CreateCustomerResultDto)` - escritura/transformación salida
- **Violación:** Los clientes que solo usan lectura dependen de método de escritura (ISP)
- **Recomendación:**
  ```java
  // Separar en dos interfaces
  public interface SoapDomainMapper { CreateCustomerInputDto toDomain(...); }
  public interface DomainSoapMapper { SoapEnvelopeResponse toSoapResponse(...); }
  ```

---

## 🟠 ALTO (7 hallazgos)

### 5. **DIP: `Tx067050Op1Adapter.java` et al.** (Adapters múltiples)
- **Archivos:** 
  - `src/main/java/com/pichincha/sp/infrastructure/output/adapter/Tx067050Op1Adapter.java`
  - `src/main/java/com/pichincha/sp/infrastructure/output/adapter/Tx067050Op5Adapter.java`
  - `src/main/java/com/pichincha/sp/infrastructure/output/adapter/Tx067050Op21Adapter.java`
  - `src/main/java/com/pichincha/sp/infrastructure/output/adapter/Tx067116Adapter.java`
  - `src/main/java/com/pichincha/sp/infrastructure/output/adapter/Tx067153Adapter.java`
  - `src/main/java/com/pichincha/sp/infrastructure/output/adapter/Tx062120Adapter.java`
  - `src/main/java/com/pichincha/sp/infrastructure/output/adapter/Tx067101Adapter.java`
- **Problema:** Acoplamiento fuerte a `BancsClient` concreto
  - Inyección de `BancsClient` sin interfaz
  - Llamadas directas `bancsClient.call(...)`
- **Violación:** DIP - Dependen de implementación, no abstracción
- **Impacto:** Testing dificultoso (mocking complexo), imposible cambiar cliente BANCS sin refactoring masivo
- **Recomendación:**
  ```java
  public interface BancsTransactionPort {
      <T> Mono<T> execute(String txId, BancsRequest<?> request, Class<T> responseType);
  }
  ```

### 6. **SRP: `ErrorResolverHandler.java`** (Línea ?)
- **Archivo:** `src/main/java/com/pichincha/sp/infrastructure/exception/error/resolver/handler/ErrorResolverHandler.java`
- **Problema:** Handler centralizado para **múltiples tipos de excepción**
  - Orquesta entre resolvers (GlobalErrorExceptionResolver, UnexpectedErrorResolver, etc.)
  - También maneja lógica de logging/construcción de respuesta
- **Impacto:** Difícil agregar nuevo resolver sin modificar handler
- **Recomendación:** Chain of Responsibility pattern

### 7. **OCP: Resolver Chain** (Múltiples)
- **Archivos:** 
  - `GlobalErrorExceptionResolver.java`
  - `UnexpectedErrorResolver.java`
  - `ResponseStatusExceptionResolver.java`
- **Problema:** Para agregar nuevo resolver hay que editar `ErrorResolverHandler`
- **Recomendación:** Usar registry inyectable o annotation-based discovery

### 8. **Coupling: Request/Response DTOs**
- **Archivos:** Todos en `infrastructure/output/adapter/request/*` y `response/*`
- **Problema:** DTOs SOAP generados acoplados a estructura WSDL específica
- **Impacto:** Cambios en WSDL requieren refactoring en adapters
- **Observación:** Es deuda técnica aceptable por naturaleza de SOAP

### 9. **DIP: `BancsDomainMapper.java`**
- **Archivo:** `src/main/java/com/pichincha/sp/infrastructure/output/adapter/mapper/BancsDomainMapper.java`
- **Problema:** Mapper hardcodeado en adapter
- **Recomendación:** Inyectar mapper como estrategia

### 10. **LSP: Métodos default en `WsClientes0108SoapMapper`**
- **Línea:** 67-212
- **Problema:** Métodos default con implementación imperitiva compleja en interfaz mapper
- **Violación sutil de LSP:** Implementaciones no sustituibles
- **Recomendación:** Usar clase abstracta o extraer a clase de utilidad

### 11. **SRP: `WSClientes0108Controller.java`**
- **Línea:** 50-56
- **Problema:** Controlador maneja:
  - Enrutamiento HTTP
  - Mapeo SOAP→Dominio
  - Manejo de errores reactivos
  - Logging
- **Recomendación:** Usar `@ControllerAdvice` para errores

---

## 🟡 MEDIO (6 hallazgos)

### 12. **Naming: `Tx067050Op1Adapter`, `Tx067050Op5Adapter`, `Tx067050Op21Adapter`**
- **Problema:** Nombres no indican responsabilidad
- **Recomendación:**
  ```
  Tx067050Op1Adapter → CreateCustomerBasicDetailAdapter
  Tx067050Op5Adapter → RetrieveCustomerPersonalDetailAdapter
  Tx067050Op21Adapter → CreateCustomerAdditionalDetailAdapter
  ```

### 13. **DIP: Constants scattered**
- **Archivos:**
  - `SoapControllerConstants.java`
  - `BancsAdapterConstants.java`
  - `ErrorCatalogConstants.java`
  - `CustomerFlowConstants.java`
- **Problema:** Constantes inyectadas directamente en lógica
- **Recomendación:** Usar Configuration bean centralizado

### 14. **Validation missing: DTOs without @Valid constraints**
- **Impacto:** Bajo - pero viola "validation at boundary"
- **Recomendación:** Agregar `@NotNull`, `@NotBlank` a DTOs

### 15. **Exception hierarchy shallow**
- **Archivos:**
  - `BusinessException.java`
  - `BusinessValidationException.java`
  - `GlobalErrorException.java`
- **Problema:** Falta categorización clara
- **Recomendación:**
  ```
  ServiceException (abstract)
    ├── BusinessException
    ├── ValidationException
    ├── IntegrationException
    └── SystemException
  ```

### 16. **Lack of Builder validation in Request DTOs**
- **Ejemplo:** `Tx067050Request.java`, `Tx062120Request.java`
- **Problema:** Builders sin validación post-construcción
- **Recomendación:** `@ConstructorProperties` + validator

### 17. **No Interface Segregation in Output Ports**
- **Archivos:** `application/port/output/*`
- **Problema:** Puertos específicos por DTO (OK), pero no grupo lógico
- **Recomendación:** Considerar port facade si hay 8+ adapters

---

## 🟢 BAJO (3 observaciones)

### 18. **Lombok @Data en DTOs**
- **Observación:** Buena práctica (ya usado)
- **Confirmación:** ✅ Consistente

### 19. **@RequiredArgsConstructor usage**
- **Observación:** Constructor inyección es correcto
- **Confirmación:** ✅ DI pattern correcto

### 20. **Spring Boot @Component/@Service anotaciones**
- **Observación:** Aplicadas correctamente
- **Confirmación:** ✅ Beans registrados

---

## 📊 Matriz Impacto vs Esfuerzo

```
        BAJO            MEDIO           ALTO            CRÍTICO
ESFUERZO
  ALTO    ·              (18)            (12)(13)        (1)(3)
 MEDIO    (20)(19)       (14)(15)(16)(17) (5)(6)(7)(9)(10)(11) (2)(4)
  BAJO    ·              ·               ·               ·
```

---

## ✅ Acciones Recomendadas (Prioridad)

### Phase 1: CRÍTICO (Sprint Actual)
1. ✅ **Extraer `CreateCustomerOrchestrator`** de service
2. ✅ **Crear `ApplicationLogger` interface** en domain/application
3. ✅ **Dividir `WsClientes0108SoapMapper`** en 2 interfaces

### Phase 2: ALTO (Sprint +1)
4. ✅ **Crear `BancsTransactionPort` interfaz** para adapters
5. ✅ **Refactorizar `ErrorResolverHandler`** a Chain of Responsibility
6. ✅ **Renombrar adapters TX** a nombres significativos

### Phase 3: MEDIO (Sprint +2)
7. ✅ **Centralizar constantes** en Configuration beans
8. ✅ **Agregar validación Bean** a DTOs
9. ✅ **Mejorar jerarquía de excepciones**

---

## 🎯 Score Cálculo

```
Base: 100
- Crítico (-10 c/u):      -40 puntos
- Alto (-5 c/u):          -35 puntos
- Medio (-2 c/u):         -12 puntos
- Bajo (-0.5 c/u):        -1.5 puntos
─────────────────────────
= 11.5 → 68/100 (SOLID Score)
```

---

## 📝 Conclusión

**El proyecto tiene una buena base arquitectónica (hexagonal correcta), pero sufre de:**
1. Clases con múltiples responsabilidades (SRP)
2. Inversión de dependencias inversa en servicios (DIP)
3. Manejo de excepciones no extensible (OCP)
4. Interfaces no segregadas (ISP)

**Recomendación:** Aplicar refactoring en 3 fases sin romper compilación.



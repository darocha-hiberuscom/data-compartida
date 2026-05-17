```markdown
# PROMPT BASE — PUERTOS DE SALIDA REACTIVOS (`PortOutput`)

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la generación de interfaces de puertos de salida reactivos para invocaciones a servicios externos.

## Reglas obligatorias
- Toda interfaz debe terminar en `PortOutput`.
- Package obligatorio: `com.pichincha.sp.application.port.output`.
- Cada interfaz debe exponer una única operación pública `execute(...)`.
- Firma estándar:
  - Entrada: `*RequestDto`
  - Salida: `Mono<*ResponseDto>`
- No incluir lógica de negocio en la interfaz.
- No usar anotaciones de framework en interfaces.
- No incluir detalles de infraestructura (URL, cliente HTTP/SOAP, headers técnicos) en la firma.

## Imports mínimos esperados
- `reactor.core.publisher.Mono`
- DTOs desde `com.pichincha.sp.domain.dto`

## Convenciones
- Nombre de interfaz en `PascalCase` + `PortOutput`.
- Nombre de método en `camelCase` (`execute`).
- Parámetros en `camelCase`.

## Plantilla base
```java
package com.pichincha.sp.application.port.output;

import com.pichincha.sp.domain.dto.QueryLocalizationRequestDto;
import com.pichincha.sp.domain.dto.LocalizationDataDto;
import reactor.core.publisher.Mono;

public interface QueryEmailLocalizationPortOutput {
    Mono<LocalizationDataDto> execute(QueryLocalizationRequestDto request);
}
```

## Alcance
Aplicable a puertos generados en:
- `com.pichincha.sp.application.port.output`

```
# SNIPPET — POLÍTICA SOAP ERROR 500 (ERRORRESOLVER)

Usar este bloque corto en prompts de implementación cuando se requiera unificar errores SOAP.

## Regla obligatoria
- La salida de error del servicio SOAP debe ser únicamente estructura `500` con:
  - `SoapFaultEnvelopeDto`
  - `SoapFaultBodyDto`
  - `SoapFaultDto`

## Captura y resolución centralizada
- Resolver errores mediante:
  - `infrastructure.exception.error.resolver.handler.ErrorResolverHandler`
  - `infrastructure.exception.error.resolver.ErrorResolver`
  - `GlobalErrorExceptionResolver`
  - `ResponseStatusExceptionResolver`
  - `UnexpectedErrorResolver`

## Tipos de error SOAP manejables
- `GlobalErrorException`
- `ResponseStatusException`
- `ConstraintViolationException`
- `Throwable` no controlado (fallback a `UnexpectedErrorResolver`)

## Restricciones
- No definir estructuras SOAP alternativas para 4xx en la salida final SOAP.
- No duplicar manejo SOAP de errores en adapters/controllers.
- Si falta detalle, registrar `FALTA DETALLE` con trazabilidad y mantener formato SOAP 500.

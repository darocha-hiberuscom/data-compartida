# SNIPPET MIN — SOAP ERROR 500

- Salida de error SOAP final: solo `500` con `SoapFaultEnvelopeDto` (`SoapFaultBodyDto` + `SoapFaultDto`).
- Resolución centralizada obligatoria: `ErrorResolverHandler` + `ErrorResolver`.
- Resolvers permitidos: `GlobalErrorExceptionResolver`, `ResponseStatusExceptionResolver`, `UnexpectedErrorResolver`.
- Errores manejables: `GlobalErrorException`, `ResponseStatusException`, `ConstraintViolationException`, `Throwable` fallback.
- No crear estructuras SOAP alternativas para `4xx`.
- No duplicar manejo SOAP de errores en adapters/controllers.
- Si falta detalle, registrar `FALTA DETALLE` manteniendo formato SOAP `500`.

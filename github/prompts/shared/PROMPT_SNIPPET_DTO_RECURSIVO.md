# SNIPPET REUTILIZABLE — DTO RECURSIVO COMPLETO

## Objetivo
Asegurar que el análisis/documentación de DTOs no omita clases internas y detalle todas las estructuras anidadas de forma recursiva.

## Instrucción corta para pegar en prompts
```markdown
### Regla obligatoria — DTOs recursivos completos
- Modelar DTOs de request/response de forma recursiva completa.
- No omitir clases internas/anidadas en ningún nivel (hijo, nieto, bisnieto, etc.).
- Si un campo usa `List<AlgoDTO>`, declarar explícitamente `AlgoDTO` con sus campos.
- No se acepta salida resumida incompleta.
- Si falta evidencia de un tipo/campo: marcar `FALTA DETALLE` + `Pista: <archivo/procedimiento>`.

Formato mínimo obligatorio:

#### Servicio: <NombreServicio>
**DTO Request:** <NombreRequestDTO>
- <campoRaiz>: <TipoAnidadoDTO>

**Desglose recursivo completo (sin omitir clases internas):**
- <NombreRequestDTO>
  - <campoA>: <TipoADTO>
- <TipoADTO>
  - <campoB>: List<<TipoBDTO>>
- <TipoBDTO>
  - <campoSimple>: <TipoJava>

**DTO Response:** <NombreResponseDTO>
- `error: ErrorResponseGeneralDTO`
- Si existe `bodyOut/headerOut`, desglosar también recursivamente con el mismo patrón.
```

## Checklist rápido de validación
- [ ] Se declaró la clase DTO raíz request
- [ ] Se declararon todas las clases anidadas directas
- [ ] Se declararon todas las clases de listas (`List<...DTO>`)
- [ ] Se declararon clases anidadas de niveles sucesivos
- [ ] Response incluye desglose recursivo cuando aplica
- [ ] Los faltantes están marcados como `FALTA DETALLE` + `Pista`

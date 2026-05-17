```markdown
# PROMPT BASE — DTO JAVA CON RECORDS

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la generación de DTOs Java usando `record` en este proyecto.

## Snippet reutilizable
- Para pegar una versión corta de esta regla en nuevos prompts, usar:
        - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`

## Reglas obligatorias
- Todos los DTOs deben implementarse como `record`.
- El nombre de todo DTO debe terminar obligatoriamente con postfijo `Dto`.
- No agregar lógica de negocio dentro de DTOs.
- No usar Lombok en DTOs (`@Getter`, `@Setter`, `@Builder`, `@Data`, etc.).
- No declarar campos mutables fuera de la firma del `record`.
- Generar DTOs de `request` y `response` para cada contrato requerido.
- Los DTOs de `response` no pueden limitarse a un subconjunto mínimo (por ejemplo solo `error.codigo`) si existe más evidencia en contratos/mapeos.
- Incluir todos los campos, subestructuras y colecciones documentados en la fuente de verdad del flujo (levantamiento, WSDL/XSD y mapeos).
- El modelado de DTOs debe ser recursivo completo: todas las clases internas/anidadas deben declararse explícitamente, sin omitir niveles intermedios.
- Si un campo referencia un tipo DTO anidado, ese tipo debe definirse explícitamente con sus campos (o `FALTA DETALLE` + `Pista` si no hay evidencia).
- Si un campo es colección (`List<...Dto>`), la clase item también debe declararse explícitamente con sus campos.
- No aceptar salidas resumidas incompletas (ejemplo inválido: declarar `A -> List<B>` sin definir `B`).

## Convenciones recomendadas
- Nombre de `record` en `PascalCase` con postfijo obligatorio `Dto`.
- Componentes del `record` en `camelCase`.
- Usar `List<...>` para colecciones.
- Si un tipo no es claro, usar `String` y dejar:
  - `// TODO: verificar tipo en contrato`

## Convención de rutas y colecciones
- Cuando la fuente de verdad incluya rutas de campos, interpretarlas en formato `dot.path`.
- No usar ni propagar sufijo `[]` en nombres/rutas de campos; la colección se representa únicamente con `List<...>`.
- Mantener nomenclatura consistente de tipos DTO anidados (misma convención de sufijo en todo el flujo).
- Si llega evidencia antigua con rutas que contienen `[]`, normalizar a ruta sin `[]` antes de modelar el DTO.

## Regla de recursividad (obligatoria)
- Generar un desglose recursivo por servicio con este orden mínimo:
        1. DTO raíz de request
        2. DTOs anidados directos
        3. DTOs anidados de segundo nivel (nietos)
        4. Niveles sucesivos hasta agotar estructuras
- Aplicar la misma regla al response cuando haya `bodyOut`, `headerOut` u objetos anidados de salida.
- Si no existe evidencia de `bodyOut`/subestructuras de salida:
        - Registrar explícitamente `FALTA DETALLE`
        - Incluir `Pista` del archivo/procedimiento/contrato donde completar

Formato de salida recomendado para documentación:
```markdown
**Desglose recursivo completo (sin omitir clases internas):**
- NombreRequestDto
        - campoRaiz: TipoAnidadoDto
- TipoAnidadoDto
        - listaItems: List<ItemDto>
- ItemDto
        - campo: String
```

## Regla de completitud (request/response)
- Antes de generar DTOs, consolidar campos desde todas las secciones relevantes de la fuente de verdad (no solo desde un resumen).
- Si una tabla de DTO presenta salida parcial, complementar con evidencia de mapeo de salida, manejo de respuesta y contrato.
- Si un campo de salida está referenciado en rutas de mapeo (`service.salida.*`, `service.error.*`, `bodyOut.*`, `error.*`), debe modelarse en el `ResponseDto` correspondiente.
- Cuando no exista contrato dependiente completo, mantener el campo identificado y marcar tipo conservador con `TODO`.

## Plantilla base
```java
import java.util.List;

public record EjemploDto(
        String campo,
        List<ItemDto> items
) {}

public record ItemDto(
        String valor
) {}
```

## Alcance
Aplicable a DTOs generados en:
- `com.pichincha.sp.domain.dto`

Regla de ubicación:
- No usar subpaquetes `orquestor` ni `ws` para DTOs generados por este flujo.

```
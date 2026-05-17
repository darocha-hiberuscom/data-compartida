```markdown
# PROMPT BASE — DTO JAVA CON LOMBOK (GETTER/SETTER)

> Referencia de secuencia: `.github/prompts/implementacion/00_orden_ejecucion_implementacion.md`

## Objetivo
Estandarizar la generación de DTOs Java usando Lombok en este proyecto.

## Reglas obligatorias
- Todos los DTOs deben usar Lombok.
- Usar explícitamente:
  - `@Getter`
  - `@Setter`
  - `@Builder`
  - `@AllArgsConstructor`
  - `@NoArgsConstructor`
- **NO usar `@Data`**.
- El nombre de toda clase DTO debe terminar obligatoriamente con postfijo `Dto`.
- No agregar lógica de negocio dentro de DTOs.
- Los DTOs deben contener únicamente atributos (y clases anidadas DTO cuando aplique).

## Convenciones recomendadas
- Nombre de clase en `PascalCase` con postfijo obligatorio `Dto`.
- Nombre de atributos en `camelCase`.
- Usar `List<...>` para colecciones.
- Si un tipo no es claro, usar `String` y dejar:
  - `// TODO: verificar tipo en contrato`

## Plantilla base
```java
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EjemploDto {

    private String campo;
}
```

## Alcance
Aplicable a DTOs generados en:
- `com.pichincha.sp.domain.dto`

Regla de ubicación:
- No usar subpaquetes `orquestor` ni `ws` para DTOs generados por este flujo.

```
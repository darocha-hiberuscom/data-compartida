```markdown
# Skill: Levantamiento IIB — Orquestadores

## Objetivo
Levantar inventario técnico completo de un orquestador IIB/ESQL: flujos, validaciones, servicios invocados, mapeo de datos y contratos, sin modificar código.

## Entradas
- Código legacy ESQL (`repos/origen/proyecto-legado/`)
- Contratos XML SOAP (`repos/origen/servicios-dependencia/`)
- Archivos `.msgflow`, `.subflow`, `.esql`, `.wsdl`, `.xsd`

## Capacidades
- Identificar tipo de solución (ORQUESTADOR) y servicios invocados desde ESQL.
- Inventariar contratos WSDL/XSD con campos, tipos y restricciones.
- Analizar patrones ESQL del orquestador:
	- Flujos de servicio (`flujoIn.estado`)
	- Validaciones de entrada (`ValidarEntrada`)
	- Flujo principal Main (OK/error)
	- Orquestación de servicios (`OrquestarWS`, `Invocar*`)
	- Lógica de negocio por estado de flujo
- Generar pseudocódigo del flujo completo.
- Mapear APIs invocadas con entrada, salida, valores fijos y paths XML SOAP.
- Mapear entrada y salida del propio orquestador.
- Consolidar todos los mensajes de error.
- Modelar DTOs Java y generar JSON request de ejemplo por servicio.

## Reglas
- No omitir archivos ni servicios.
- No inventar información.
- Marcar faltantes con `FALTA DETALLE` + `Pista`.
- Los paths XML SOAP se obtienen de `repos/origen/servicios-dependencia/`.
- Los tipos Java se infieren desde XSD/WSDL (`xs:string` → `String`, `xs:int` → `Integer`, etc.).
- Si un campo es colección (iterado con `WHILE`/`MOVE`), usar `List<NombreDTO>`.

## Salida esperada
- `migracion/LEVANTAMIENTO_INFORMACION.md`

## Definition of Done
- Visión general con nombre del proyecto y lista de servicios identificados
- Inventario completo de WSDL y XSD con campos tipados
- Análisis ESQL con flujos, validaciones, orquestación y pseudocódigo
- Mapeo de APIs con entrada/salida/valores fijos y paths XML SOAP
- Mapeo E/S del orquestador completo
- Consolidado de errores sin omisiones
- Resumen de servicios, DTOs y JSON request por servicio
- Todos los checklists de cada prompt validados

```

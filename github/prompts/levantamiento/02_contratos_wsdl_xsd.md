# PASO 2 — INVENTARIO DE CONTRATOS (WSDL / XSD)

> Nota: este archivo corresponde a un paso ejecutable del proceso de levantamiento.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## PREREQUISITO
- Paso 1 completado (`./migracion/LEVANTAMIENTO_INFORMACION.md` existe con sección 1).

## OBJETIVO
Inventariar todos los contratos WSDL y esquemas XSD del proyecto, documentando operaciones, campos, tipos y restricciones.

## ALCANCE
- Analizar todos los archivos `.wsdl` y `.xsd` en `repos/origen/proyecto-legado`.
- Documentar operaciones con entrada, salida y ComplexType asociados.
- Extraer restricciones de campos definidas en XSD/WSDL.

## INSTRUCCIONES
1. Recorrer todos los archivos `.wsdl` del proyecto.
2. Por cada WSDL, identificar el nombre del servicio, endpoint y operaciones.
3. Por cada operación, documentar los campos de entrada y salida con tipos, obligatoriedad y restricciones.
4. Si la entrada o salida es de tipo complejo, incluir los ComplexType asociados con su estructura de campos.
5. Extraer restricciones XSD: `minOccurs`, `maxOccurs`, `length`, `minLength`, `maxLength`, `pattern`, `enumeration`, `nillable`, `default`, `fixed`, `minInclusive`, `maxInclusive`.
6. Recorrer todos los archivos `.xsd` y documentar estructuras principales, tipos complejos, reutilización de esquemas y reglas de validación.
7. En `Entrada` y `Salida`, desglosar campos de forma recursiva (todos los niveles), incluyendo subcampos de `complexType` y de XSD externos/importados.
8. Mostrar la ruta completa del campo (notación punto y `[]` para colecciones), nivel jerárquico y origen del esquema/tipo.

## FUENTES
- `repos/origen/proyecto-legado/*.wsdl`
- `repos/origen/proyecto-legado/*.xsd`

## SALIDA OBLIGATORIA
Agregar al archivo `./migracion/LEVANTAMIENTO_INFORMACION.md` las secciones 2 y 3:

### Sección 2 — INVENTARIO DE SERVICIOS (WSDL)
Por cada servicio/operación:

```markdown
### Servicio: <nombre>
- Endpoint (si aplica)
- Operaciones

#### Operación: <nombre>
##### Entrada
| Nivel | Campo (ruta completa) | Tipo | Obligatorio | minOccurs | maxOccurs | Origen (XSD/ComplexType) | Restricciones XSD/WSDL |

> Incluir **todos** los niveles: raíz, hijos, nietos, etc. No limitarse al primer nivel.

##### Salida
| Nivel | Campo (ruta completa) | Tipo | Obligatorio | minOccurs | maxOccurs | Origen (XSD/ComplexType) | Restricciones XSD/WSDL |

> Incluir **todos** los niveles: raíz, hijos, nietos, etc. No limitarse al primer nivel.

##### ComplexType asociados (si aplica)
| ComplexType | Campo | Tipo | Obligatorio | minOccurs | maxOccurs | Restricciones XSD/WSDL |
```

### Sección 3 — INVENTARIO DE XSD
```markdown
## 3. INVENTARIO DE XSD
- Estructuras principales (solo nombres)
- Tipos complejos (solo nombres; el detalle ya fue asociado en el paso 2)
- Reutilización de esquemas
- Reglas de validación por campo
```

## CHECKLIST DE VALIDACIÓN
- [ ] Todos los archivos `.wsdl` del proyecto analizados
- [ ] Operaciones documentadas con campos de entrada y salida
- [ ] Entrada y salida desglosadas de forma recursiva (todos los niveles)
- [ ] ComplexType asociados desglosados
- [ ] Restricciones XSD extraídas por campo
- [ ] Todos los archivos `.xsd` inventariados
- [ ] Tipos complejos listados
- [ ] Reutilización de esquemas identificada
- [ ] Secciones 2 y 3 agregadas al documento

## REGLAS
- NO modificar código fuente.
- NO inventar información.
- Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md`.

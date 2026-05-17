# PLAN DE LEVANTAMIENTO — ORQUESTADOR LEGACY

## OBJETIVO
Ejecutar de forma autónoma y secuencial el levantamiento técnico completo de un orquestador legacy (IIB/ESQL), dividido en pasos independientes con validación.

## AGENTE REQUERIDO
- `agente_analista_legacy`

## FUENTES DE DATOS
- **Código fuente**: `repos/origen/proyecto-legado` (archivos `.esql`, `.msgflow`, `.subflow`)
- **Dependencias de servicios**: `repos/origen/servicio-dependencia` (XML SOAP, URLs, requests)
- **Contratos**: `repos/origen/proyecto-legado` (archivos `.wsdl`, `.xsd`)

## SALIDA
Todos los pasos generan su resultado en `./migracion/LEVANTAMIENTO_INFORMACION.md` (raíz del workspace).
Cada paso agrega sus secciones al documento. NO crear archivos separados.

---

## SECUENCIA DE EJECUCIÓN

### Paso 1 → `01_vision_general.md`
**Alcance**: Visión general del proyecto e identificación de servicios principales.
**Prerequisito**: Ninguno.
**Checklist de validación**:
- [ ] Nombre del proyecto identificado
- [ ] Tipo de solución = ORQUESTADOR
- [ ] Lista de servicios identificados desde los `.esql`
- [ ] Secciones 1 del documento generadas

### Paso 2 → `02_contratos_wsdl_xsd.md`
**Alcance**: Inventario de WSDL y XSD con campos, tipos y restricciones.
**Prerequisito**: Paso 1 completado.
**Checklist de validación**:
- [ ] Todos los WSDL del proyecto inventariados
- [ ] Operaciones documentadas con entrada/salida/ComplexType
- [ ] Restricciones XSD extraídas
- [ ] Inventario XSD completo
- [ ] Secciones 2 y 3 del documento generadas

### Paso 3 → `03_analisis_esql_flujos_validaciones.md`
**Alcance**: Análisis de ESQL — flujos, validaciones, Main y orquestación.
**Prerequisito**: Paso 1 completado.
**Checklist de validación**:
- [ ] Flujos del servicio identificados (4.1)
- [ ] Validaciones de campos documentadas (4.2)
- [ ] Flujo Main documentado (4.3)
- [ ] Secuencia de orquestación por estado de flujo (4.4)
- [ ] Lógica de negocio por flujo (4.5)
- [ ] Pseudocódigo generado (4.6)
- [ ] Sección 4 (4.1 a 4.6) del documento generada

### Paso 4 → `04_mapeo_apis_servicios.md`
**Alcance**: Mapeo detallado de cada API invocada con campos, valores fijos y paths XML.
**Prerequisito**: Paso 3 completado.
**Checklist de validación**:
- [ ] Todas las APIs/procedimientos `Invocar*()` documentados
- [ ] Mapeo de entrada por API con origen y valores fijos
- [ ] Mapeo de salida por API
- [ ] Manejo de respuesta (éxito/error) documentado
- [ ] Paths XML SOAP origen y destino completados
- [ ] Sección 4.7 del documento generada

### Paso 5 → `05_mapeo_entrada_salida_errores.md`
**Alcance**: Mapeo de entrada/salida del orquestador y consolidado de errores.
**Prerequisito**: Pasos 3 y 4 completados.
**Checklist de validación**:
- [ ] Todos los campos de entrada del orquestador documentados (4.8)
- [ ] Todos los campos de salida del orquestador documentados (4.9)
- [ ] Consolidado de errores completo (4.10)
- [ ] Secciones 4.8, 4.9 y 4.10 del documento generadas

### Paso 6 → `06_resumen_servicios_dtos.md`
**Alcance**: Resumen global de servicios, DTOs, clases candidatas y JSON request de ejemplo.
**Prerequisito**: Pasos 4 y 5 completados.
**Checklist de validación**:
- [ ] Tabla consolidada de servicios invocados (sección 12)
- [ ] DTOs de entrada/salida por servicio (sección 13)
- [ ] JSON request de ejemplo por servicio (sección 14)
- [ ] Clases candidatas por servicio basadas en WSDL + request/response de servicios (sección 15)
- [ ] Clase general de error `ErrorResponseGeneralDTO` incluida en clases candidatas
- [ ] Secciones 12, 13, 14 y 15 del documento generadas

---

## REGLAS DE EJECUCIÓN
1. Ejecutar los pasos en orden secuencial (1 → 2 → 3 → 4 → 5 → 6).
2. Cada paso debe completar su checklist antes de pasar al siguiente.
3. Si un paso no puede completarse, marcar `FALTA DETALLE` con la pista y continuar.
4. NO modificar las secciones generadas por pasos anteriores.
5. NO inventar información. Si no se encuentra evidencia, marcar explícitamente.
6. Aplicar reglas de `.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md` en todos los pasos.

## REFERENCIA TÉCNICA DE AUTOMATIZACIÓN
- Si se requiere crear scripts auxiliares para búsqueda, extracción o consolidación de información durante la ejecución de prompts, usar **Node.js**.
- Evitar scripts en Python para este flujo, ya que los ambientes objetivo pueden no tener Python instalado.
- Priorizar ejecución con `node` y dependencias del ecosistema JavaScript/Node cuando aplique.

## NOTA GLOBAL — DTOs RECURSIVOS
- Para todo paso que genere DTOs o clases candidatas (especialmente paso 6), aplicar:
      - `.github/prompts/shared/PROMPT_BASE_DTO_RECORDS.md`
      - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`
- Regla obligatoria: modelado recursivo completo, sin omitir clases internas/anidadas en ningún nivel.

## DIAGRAMA DE DEPENDENCIAS

```
Paso 1 (Visión General)
  ├── Paso 2 (Contratos WSDL/XSD)
  └── Paso 3 (Análisis ESQL)
          └── Paso 4 (Mapeo APIs)
                └── Paso 5 (Mapeo E/S + Errores)
                      └── Paso 6 (Resumen + DTOs + Clases Candidatas + JSON)
```

---

## EJECUCIÓN AUTÓNOMA

Para ejecutar TODO el levantamiento de forma autónoma en una sola instrucción, usa el siguiente prompt en el chat de Copilot:

```
@workspace Ejecuta de forma autónoma el plan de levantamiento completo del orquestador.

## Contexto
- Plan: #file:.github/plans/plan_levantamiento_orquestador.md
- Agente: #file:.github/agents/agente_analista_legacy.md
- Skill: #file:.github/skills/skill_levantamiento_iib.md
- Instrucciones: #file:.github/instructions/analisis.md
- Reglas de evidencia: #file:.github/prompts/shared/PROMPT_BASE_EVIDENCIA_TRASABILIDAD.md

## Instrucciones de ejecución
1. Lee el plan completo y el agente para entender el alcance.
2. Ejecuta los pasos en orden secuencial (1 → 2 → 3 → 4 → 5 → 6).
3. Para cada paso:
   a. Lee el prompt correspondiente en .github/prompts/levantamiento/
      b. Analiza los archivos fuente indicados (repos/origen/proyecto-legado/ y repos/origen/servicio-dependencia/)
   c. Genera las secciones correspondientes en ./migracion/LEVANTAMIENTO_INFORMACION.md
   d. Valida el checklist del paso antes de continuar al siguiente
4. Si falta evidencia, marca FALTA DETALLE + Pista y continúa.
5. NO modifiques código fuente. NO inventes información.
6. Al finalizar todos los pasos, presenta un resumen de completitud con el estado de cada checklist.
```

### Notas de ejecución autónoma
- El agente procesará los 6 pasos secuencialmente sin intervención.
- Si el contexto es muy grande, puede requerir múltiples turnos; en ese caso retomará desde el último paso completado.
- Valida que las carpetas `repos/origen/proyecto-legado/` y `repos/origen/servicio-dependencia/` contengan los archivos del proyecto antes de lanzar.

````markdown
# IMPLEMENTACIÓN 00 — ORDEN OFICIAL DE EJECUCIÓN

> Nota: este archivo define la secuencia oficial para ejecutar los pasos de implementación sin desorden.

## OBJETIVO
Establecer el orden obligatorio de ejecución de los pasos 01–15 para una migración consistente y trazable.

## ORDEN OBLIGATORIO
1. `01_generar_dtos_desde_levantamiento.md`
2. `02_generar_puertos_entrada.md`
3. `03_generar_puertos_salida.md`
4. `04_generar_modelos_adapter_output_request_response.md`
5. `05_generar_mappers_dominio_adapter_output.md`
6. `06_generar_adapters_output_webclient.md`
7. `07_configurar_webclient_output.md`
8. `08_manejo_errores_tecnicos_output_webclient.md`
9. `09_generar_service_orquestacion_port_input.md`
10. `10_completar_controller_input_adapter_rest.md`
11. `11_generar_presenter_response_mapper_input.md`
12. `12_generar_validaciones_legado_desde_levantamiento.md`
13. `13_acoplar_modelo_excepciones_input.md`
14. `14_configurar_application_yaml.md`
15. `15_validar_application_yaml_vs_helm_configmap.md`

## PLAN DE IMPLEMENTACIÓN
- Plan estructurado por fases y entregables:
  - `.github/plans/PLAN_IMPLEMENTACION_01_15.md`

## REGLAS DE EJECUCIÓN
- No saltar pasos.
- No ejecutar un paso si su prerequisito no está completado.
- Si un paso marca `FALTA DETALLE`, resolver evidencia antes de continuar con pasos dependientes.
- Mantener trazabilidad con `./migracion/LEVANTAMIENTO_INFORMACION.md` como fuente principal.
- Regla transversal de código: evitar al máximo la duplicidad; preferir reutilización de componentes, utilitarios, mappers y estrategias existentes antes de crear lógica repetida.
- Regla transversal de calidad: generar código orientado a minimizar issues de Sonar (duplicidad, complejidad ciclomática excesiva, métodos largos, null-safety, manejo de excepciones, nombres claros y eliminación de código muerto).

## NOTA GLOBAL — DTOs RECURSIVOS
- Para todo paso que genere DTOs o modelos request/response, aplicar:
  - `.github/prompts/shared/PROMPT_BASE_DTO_RECORDS.md`
  - `.github/prompts/shared/PROMPT_SNIPPET_DTO_RECURSIVO.md`
- Regla obligatoria: modelado recursivo completo, sin omitir clases internas/anidadas en ningún nivel.

## NOTA SOBRE MAPPERS
- Todo mapper generado en cualquier paso debe cumplir:
  - `.github/prompts/shared/PROMPT_BASE_POLITICA_MAPPERS_MAPSTRUCT.md`

## SALIDA ESPERADA
- Flujo de implementación ejecutado en secuencia, sin dependencias rotas entre capas (`application` / `infrastructure`).

````
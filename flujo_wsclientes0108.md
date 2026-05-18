# Flujo de invocación - wsclientes0108.esql

## Archivo fuente
- `wsclientes0108.esql`

## Flujo general
1. `Main()`
2. `ValidarEntrada()`
3. `OrquestarTX()`
4. Si todo es correcto, responde error `codigo = 0`, `mensaje = OK`

## Secuencia de orquestación (`OrquestarTX`)
1. `InvocarCrearDetalleCliente()`
   - TX: `067050`
   - Opción: `1`
   - Secuencia: primero ejecuta `TX067050` opción `1`
   - Luego: actualiza con `TX067000`
2. `InvocarDetallePersonales()`
   - TX: `067050`
   - Opción: `5`
   - Salida: la respuesta se usa como entrada de la siguiente transacción
3. `InvocarDetallePersonalesRespuesta()`
   - TX: `067101`
   - Entrada: respuesta de `TX067050`
4. `InvocarCrearDatosAdicionales()`
   - TX: `067050`
   - Opción: `21`
   - Luego: invoca `TX067116`
5. `ActualizarDatosIdentificacion()`
   - Invoca: `ConsultarTX067153`
   - Luego: `ModificarTX067153`
   - Condición: `IF expuestoPolitico IS NOT NULL AND TRIM(expuestoPolitico) <> '' THEN`
     - Invoca: `ConsultarTX067050OP21`
     - Luego: `ActualizarTX067116`
6. `Operacionfatca('1')`
   - TX: `062120`
   - Operación: actualización FATCA/correspondencia
   - Regla especial: si retorna código `8783`, reintenta con `Operacionfatca('3')`

## Cómo invoca técnicamente (UMP/TX)
Para cada llamada UMP, el patrón es:
1. Asignar `Environment.UMPSubflow.ump`
2. Asignar `Environment.UMPSubflow.metodo`
3. Asignar `Environment.UMPSubflow.configurable = FALSE`
4. Construir `bodyIn` en `Environment.UMPSubflow.<metodo>`
5. Ejecutar `PROPAGATE TO LABEL 'et_ump'`
6. Leer respuesta en `Environment.UMPSubflow.<metodo>`
7. Validar `error.codigo`

Para los pasos por TX (`067050`, `067000`, `067101`, `067116`, `067153` y `062120`), la invocación aplica el contrato transaccional correspondiente en lugar del patrón `UMPSubflow`, incluyendo la secuencia `ConsultarTX067153 -> ModificarTX067153` y el flujo condicional por `expuestoPolitico`.

## Ajuste solicitado
- El primer paso del flujo queda definido con secuencia `TX067050` (opción `1`) y luego actualización con `TX067000` en reemplazo de `UMPClientes0110`.
- El paso que usaba `UMPClientes0111` se reemplaza por `TX067050` y, con su respuesta, se invoca `TX067101`.
- El paso que usaba `UMPClientes0112` se reemplaza por `TX067050` (opción `21`) y luego `TX067116`.
- El paso que usaba `UMPClientes0013` se reemplaza por `ConsultarTX067153` y luego `ModificarTX067153`; si `expuestoPolitico` tiene valor, además invoca `ConsultarTX067050OP21` y luego `ActualizarTX067116`.
- El paso que usaba `UMPClientes0066` se reemplaza por `TX062120`.

## Manejo de error
- Si una invocación falla (`error.codigo <> '0'`), carga `Environment.salida.error` y corta flujo.
- En `Operacionfatca`, hay lógica de reintento específica para `8783`; para otros casos también retorna error.

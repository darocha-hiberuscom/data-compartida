package com.pichincha.sp.application.port.output;

/**
 * Port de logging para application layer.
 * Abstracción que desacopla application de infraestructura de logging.
 * Implementación en infrastructure.logging.
 */
public interface ApplicationLogger {

    /**
     * Registra finalización del flujo.
     *
     * @param signal Tipo de señal (COMPLETE, ERROR, CANCEL, etc.)
     */
    void logFlowFinished(String signal);

    /**
     * Registra error en el flujo.
     *
     * @param message Descripción del error
     */
    void logFlowError(String message);

    /**
     * Registra inicio del flujo.
     *
     * @param message Descripción del evento
     */
    void logFlowStarted(String message);
}


package com.pichincha.sp.infrastructure.constants;

import org.springframework.http.HttpStatus;

public final class BancsAdapterConstants {

    public static final String SERVICE_NAME = "WS_Clientes0108";

    // Transaction IDs
    public static final String TX_067050 = "067050";
    public static final String TX_067000 = "067000";
    public static final String TX_067101 = "067101";
    public static final String TX_067116 = "067116";
    public static final String TX_067153 = "067153";
    public static final String TX_062120 = "062120";

    // TX067050: la opcion viaja como segmento de URL /{tx}-{opcion}
    public static final String TX_067050_OP_1  = TX_067050 + "-1";
    public static final String TX_067050_OP_5  = TX_067050 + "-5";
    public static final String TX_067050_OP_21 = TX_067050 + "-21";

    // Log messages
    public static final String LOG_BANCS_CLIENT_EXCEPTION =
            "Adaptador Bancs: Error de integracion con BancsAPIClientException en TX{}: {}";
    public static final String LOG_UNEXPECTED_ERROR =
            "Adaptador Bancs: Error inesperado en TX{}: {}";
    public static final String LOG_FLOW_FINISHED =
            "Adaptador Bancs: Finalizando TX{}. Signal: {}";

    // Error messages por TX
    public static final String ERROR_TX067050_OP1_HEADER  = "Error en servicio TX067050 opcion 1";
    public static final String ERROR_TX067050_OP5_HEADER  = "Error en servicio TX067050 opcion 5";
    public static final String ERROR_TX067050_OP21_HEADER = "Error en servicio TX067050 opcion 21";
    public static final String ERROR_TX067000_HEADER      = "Error en servicio TX067000";
    public static final String ERROR_TX067101_HEADER      = "Error en servicio TX067101";
    public static final String ERROR_TX067116_HEADER      = "Error en servicio TX067116";
    public static final String ERROR_TX067153_HEADER      = "Error en servicio TX067153";
    public static final String ERROR_TX062120_HEADER      = "Error en servicio TX062120";

    public static final HttpStatus DEFAULT_ERROR_STATUS = HttpStatus.INTERNAL_SERVER_ERROR;

    public static final String SUCCESS_RESPONSE_CODE  = "0";
    public static final String DEFAULT_ERROR_CODE     = "UNKNOWN";
    public static final String DEFAULT_ERROR_MESSAGE  = "No message returned by BANCS";
    public static final String BUSINESS_ERROR_TEMPLATE =
            "BANCS business error for TX%s - bancsErrorCode=%s, message=%s";
    public static final String NULL_BODY_ERROR_TEMPLATE =
            "BANCS response body is null for transaction %s";

    private BancsAdapterConstants() {
    }
}

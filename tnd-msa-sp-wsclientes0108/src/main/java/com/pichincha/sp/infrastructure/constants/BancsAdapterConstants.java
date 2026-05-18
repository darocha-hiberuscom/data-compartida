package com.pichincha.sp.infrastructure.constants;

public final class BancsAdapterConstants {

    public static final String SERVICE_NAME = "WS_Clientes0108";
    public static final String TX_067050 = "067050";
    public static final String TX_067000 = "067000";
    public static final String TX_067101 = "067101";
    public static final String TX_067116 = "067116";
    public static final String TX_067153 = "067153";
    public static final String TX_062120 = "062120";

    public static final String LOG_RESPONSE_RECEIVED = "BANCS response received for tx {}";
    public static final String LOG_VALIDATION_SUCCESS = "BANCS validation successful for tx {}";
    public static final String LOG_MAPPING_SUCCESS = "BANCS mapping successful for tx {}";
    public static final String LOG_HTTP_ERROR = "BANCS http error for tx {}: {}";
    public static final String LOG_CONNECTION_ERROR = "BANCS connection error for tx {}: {}";
    public static final String LOG_TIMEOUT_ERROR = "BANCS timeout error for tx {}: {}";
    public static final String LOG_UNEXPECTED_ERROR = "BANCS unexpected error for tx {}: {}";
    public static final String LOG_FLOW_FINISHED = "BANCS adapter flow finished for tx {} with signal {}";

    private BancsAdapterConstants() {
    }
}

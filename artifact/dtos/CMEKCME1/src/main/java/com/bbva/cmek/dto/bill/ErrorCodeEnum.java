package com.bbva.cmek.dto.bill;

//Enum que tiene todos los posibles codigos de error que se manejaran en la unidad de despliegue
public enum ErrorCodeEnum {
    CMEK12000001("CMEK12000001", "Factura No Encontrada"),
    CMEK12000002("CMEK12000002", "Monto Factura y Monto Ingresado Diferentes - CMEK12000002"),
    CMEK12000003("CMEK12000003", "Factura No Valida"),
    CMEK12000004("CMEK12000004", "Cuenta No Apta Para Pago");

    private final String codError;
    private final String msgError;

    // Constructor
    ErrorCodeEnum(String codError, String msgError) {
        this.codError = codError;
        this.msgError = msgError;
    }

    // Métodos para obtener los valores
    public String getCodError() {
        return codError;
    }

    public String getMsgError() {
        return msgError;
    }

    @Override
    public String toString() {
        return codError + " - " + msgError;
    }
}
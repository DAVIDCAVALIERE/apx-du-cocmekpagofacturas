package com.bbva.cmek.dto.account;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// DTO para la respuesta de consulta de cuenta
public class GetAccountResponseDTO implements Serializable {
    private String id;
    private GetAccountBalanceDTO balance;
    private String errorCode;
    private String errorMessage;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GetAccountBalanceDTO getBalance() {
        return balance;
    }

    public void setBalance(GetAccountBalanceDTO balance) {
        this.balance = balance;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("balance", balance)
                .append("errorCode", errorCode)
                .append("errorMessage", errorMessage)
                .toString();
    }
}

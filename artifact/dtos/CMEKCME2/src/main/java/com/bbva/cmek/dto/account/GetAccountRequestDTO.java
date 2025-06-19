package com.bbva.cmek.dto.account;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// DTO para la petición de consulta de cuenta
public class GetAccountRequestDTO implements Serializable {
    private String accountNumber;

    // Getters y Setters
    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("accountNumber", accountNumber)
                .toString();
    }
}

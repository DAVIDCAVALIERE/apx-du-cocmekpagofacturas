package com.bbva.cmek.dto.account;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// DTO que representa el saldo de una cuenta consultada
public class GetAccountBalanceDTO implements Serializable {
    private long amount;

    // Getter y Setter
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("amount", amount)
                .toString();
    }
}

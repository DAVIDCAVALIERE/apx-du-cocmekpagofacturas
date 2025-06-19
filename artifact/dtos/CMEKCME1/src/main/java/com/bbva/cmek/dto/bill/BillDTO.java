package com.bbva.cmek.dto.bill;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// DTO que representa una factura a nivel funcional
public class BillDTO implements Serializable {
    private String number;
    private long amount;

    // Getters y Setters
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("number", number)
                .append("amount", amount)
                .toString();
    }
}
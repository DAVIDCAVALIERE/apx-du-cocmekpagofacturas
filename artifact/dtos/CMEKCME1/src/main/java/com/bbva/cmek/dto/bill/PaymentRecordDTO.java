package com.bbva.cmek.dto.bill;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

// DTO que representa el registro de pago en la base de datos MongoDB
public class PaymentRecordDTO implements Serializable {
    private String billNumber;
    private Date paymentDate;
    private String accountNumber;
    private long billAmount;

    // Getters y Setters
    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public long getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(long billAmount) {
        this.billAmount = billAmount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("billNumber", billNumber)
                .append("paymentDate", paymentDate)
                .append("accountNumber", accountNumber)
                .append("billAmount", billAmount)
                .toString();
    }
}
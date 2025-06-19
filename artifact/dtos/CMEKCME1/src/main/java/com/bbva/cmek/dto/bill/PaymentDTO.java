package com.bbva.cmek.dto.bill;

import com.bbva.cmek.dto.account.AccountDTO;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

// DTO que representa el pago a nivel funcional
public class PaymentDTO implements Serializable {
    private String id;
    private Date operationDateTime;
    private AccountDTO account;
    private BillDTO bill;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getOperationDateTime() {
        return operationDateTime;
    }

    public void setOperationDateTime(Date operationDateTime) {
        this.operationDateTime = operationDateTime;
    }

    public AccountDTO getAccount() {
        return account;
    }

    public void setAccount(AccountDTO account) {
        this.account = account;
    }

    public BillDTO getBill() {
        return bill;
    }

    public void setBill(BillDTO bill) {
        this.bill = bill;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("operationDateTime", operationDateTime)
                .append("account", account)
                .append("bill", bill)
                .toString();
    }
}
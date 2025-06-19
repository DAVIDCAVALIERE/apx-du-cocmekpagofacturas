
package com.bbva.cmek.dto.bill;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * The BillRecordDTO class...
 */
public class BillRecordDTO implements Serializable {
    private String billId;
    private Date generationDate;
    private Date expirationDate;
    private long billAmount;
    private String billStatus; // VIGENTE o PAGADO
    private String billUser;
    private String billProvider;
    private Date billUpdateDate;

    // Getters y Setters
    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public Date getGenerationDate() {
        return generationDate;
    }

    public void setGenerationDate(Date generationDate) {
        this.generationDate = generationDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(long billAmount) {
        this.billAmount = billAmount;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public String getBillUser() {
        return billUser;
    }

    public void setBillUser(String billUser) {
        this.billUser = billUser;
    }

    public String getBillProvider() {
        return billProvider;
    }

    public void setBillProvider(String billProvider) {
        this.billProvider = billProvider;
    }

    public Date getBillUpdateDate() {
        return billUpdateDate;
    }

    public void setBillUpdateDate(Date billUpdateDate) {
        this.billUpdateDate = billUpdateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("billId", billId)
                .append("generationDate", generationDate)
                .append("expirationDate", expirationDate)
                .append("billAmount", billAmount)
                .append("billStatus", billStatus)
                .append("billUser", billUser)
                .append("billProvider", billProvider)
                .append("billUpdateDate", billUpdateDate)
                .toString();
    }
}


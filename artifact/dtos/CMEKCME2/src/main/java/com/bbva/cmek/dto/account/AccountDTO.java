
package com.bbva.cmek.dto.account;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

// DTO que representa una cuenta a nivel funcional
public class AccountDTO implements Serializable {
    private String id;

    // Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .toString();
    }
}


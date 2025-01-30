package com.elmaguiri.backend.dao.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, exclude = "client")
@Data
@AllArgsConstructor
public class Partner extends Person {
    @Column(name = "participation_amount")
    private BigDecimal participationAmount;

    @Column(name = "participation_percentage")
    private double participationPercentage;

    public void setSomePartnerSpecificField(BigDecimal participationAmount, double participationPercentage) {
        this.participationAmount = participationAmount;
        this.participationPercentage = participationPercentage;
    }
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
}

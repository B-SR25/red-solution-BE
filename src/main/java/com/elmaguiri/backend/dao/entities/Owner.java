package com.elmaguiri.backend.dao.entities;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Owner extends Person {

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Client> clients;

    @Column(name = "some_owner_specific_field")
    private String someOwnerSpecificField;

    public String getSomeOwnerSpecificField() {
        return someOwnerSpecificField;
    }

    public void setSomeOwnerSpecificField(String someOwnerSpecificField) {
        this.someOwnerSpecificField = someOwnerSpecificField;
    }
}

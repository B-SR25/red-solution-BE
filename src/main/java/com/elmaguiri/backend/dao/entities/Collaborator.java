package com.elmaguiri.backend.dao.entities;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class Collaborator extends Person {
    private String someCollaboratorSpecificField;

    public String getSomeCollaboratorSpecificField() {
        return someCollaboratorSpecificField;
    }

    public void setSomeCollaboratorSpecificField(String someCollaboratorSpecificField) {
        this.someCollaboratorSpecificField = someCollaboratorSpecificField;
    }
}

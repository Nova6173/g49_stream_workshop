package se.lexicon.vxo.model;

import java.time.LocalDate;

public class PersonDto {

    private int personId;
    private String fullName;

    public PersonDto(int personId, String fullName, String lastName, LocalDate dateOfBirth, Gender gender) {
        this.personId = personId;
        this.fullName = fullName;
    }

    public int getPersonId() {
        return personId;
    }

    public String getFullName() {
        return fullName;
    }

}

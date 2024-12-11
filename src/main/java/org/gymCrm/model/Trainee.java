package org.gymCrm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.EqualsAndHashCode;


import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class Trainee extends User {

    @JsonProperty(value="birthDate")
    private LocalDate birthDate;
    private Address address;


    public Trainee(int id, String firstName, String lastName, String username, String password, boolean isActive, LocalDate birthDate, Address address) {
        super(id, firstName, lastName, username, password, isActive);
        this.birthDate = birthDate;
        this.address = address;
    }


}

package org.gymCrm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class User {

    @JsonProperty(value="id")
    private int id;
    @JsonProperty(value="firstName")
    private String firstName;
    @JsonProperty(value="lastName")
    private String lastName;
    @JsonProperty(value="username")
    private String username;
    @JsonProperty(value="password")
    private String password;
    @JsonProperty(value="isActive")
    private boolean isActive;


    @Override
    public String toString() {
        return
                "id= " + id +
                        ", firstname= " + firstName + '\'' +
                        ", lastname= " + lastName + '\'' +
                        ", username= " + username + '\'' +
                        ", password= " + password + '\'' +
                        ", isActive= " + isActive;
    }
}


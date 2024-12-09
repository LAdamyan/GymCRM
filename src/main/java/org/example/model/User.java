package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
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


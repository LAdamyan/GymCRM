package org.gymCrm.hibernate.dto;

import lombok.Data;

@Data
public class TokenValidationRequest {

    private String username;
    private String token;
}

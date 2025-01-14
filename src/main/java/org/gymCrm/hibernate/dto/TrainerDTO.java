package org.gymCrm.hibernate.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TrainerDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Specialization is required")
    private String specialization;


}

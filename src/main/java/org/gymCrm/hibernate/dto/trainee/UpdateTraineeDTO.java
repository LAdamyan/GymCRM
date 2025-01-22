package org.gymCrm.hibernate.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.gymCrm.hibernate.dto.address.AddressDTO;

import java.time.LocalDate;

@Data
public class UpdateTraineeDTO {
    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;
    private LocalDate birthDate;
    private AddressDTO address;
    private boolean isActive;

}

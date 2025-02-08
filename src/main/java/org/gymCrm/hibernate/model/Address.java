package org.gymCrm.hibernate.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {

    @NotNull(message = "City cannot be null")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    @Column(name="city")
    private String city;

    @NotNull(message = "Street cannot be null")
    @Size(min = 2, max = 100, message = "Street must be between 2 and 100 characters")
    @Column(name="street")
    private String street;

    @NotNull(message = "Building cannot be null")
    @Size(min = 1, max = 50, message = "Building must be between 1 and 50 characters")
    @Column(name="building")
    private String building;

    @Pattern(regexp = "\\d+", message = "Building number must be numeric")
    @Column(name="buildingNumber")
    private String buildingNumber;

}

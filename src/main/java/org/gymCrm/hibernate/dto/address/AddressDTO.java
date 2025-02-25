package org.gymCrm.hibernate.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {
    private String city;
    private String street;
    private String building;
    private String buildingNumber;
}

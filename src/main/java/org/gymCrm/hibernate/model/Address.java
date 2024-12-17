package org.gymCrm.hibernate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Address {


    @Column(name="city")
    private String city;

    @Column(name="street")
    private String street;

    @Column(name="building")
    private String building;

    @Column(name="buildingNumber")
    private String buildingNumber;

}

package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Address {

    @JsonProperty(value="city")
    private String city;
    @JsonProperty(value="street")
    private String street;
    @JsonProperty(value="building")
    private String building;
    @JsonProperty(value="buildingNumber")
    private String buildingNumber;
}

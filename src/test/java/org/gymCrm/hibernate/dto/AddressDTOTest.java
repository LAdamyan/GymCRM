package org.gymCrm.hibernate.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AddressDTOTest {
    @Test
    void testAllArgsConstructor() {
        AddressDTO address = new AddressDTO("New York", "5th Avenue", "Building A", "123");

        assertEquals("New York", address.getCity());
        assertEquals("5th Avenue", address.getStreet());
        assertEquals("Building A", address.getBuilding());
        assertEquals("123", address.getBuildingNumber());
    }

    @Test
    void testNoArgsConstructor() {
        AddressDTO address = new AddressDTO();

        address.setCity("Los Angeles");
        address.setStreet("Sunset Boulevard");
        address.setBuilding("Building B");
        address.setBuildingNumber("456");

        assertEquals("Los Angeles", address.getCity());
        assertEquals("Sunset Boulevard", address.getStreet());
        assertEquals("Building B", address.getBuilding());
        assertEquals("456", address.getBuildingNumber());
    }

    @Test
    void testSettersAndGetters() {
        AddressDTO address = new AddressDTO();

        address.setCity("Chicago");
        address.setStreet("Michigan Avenue");
        address.setBuilding("Building C");
        address.setBuildingNumber("789");

        assertEquals("Chicago", address.getCity());
        assertEquals("Michigan Avenue", address.getStreet());
        assertEquals("Building C", address.getBuilding());
        assertEquals("789", address.getBuildingNumber());

    }
    @Test
    void testEqualsAndHashCode() {

        AddressDTO address1 = new AddressDTO("San Francisco", "Market Street", "Building D", "101");
        AddressDTO address2 = new AddressDTO("San Francisco", "Market Street", "Building D", "101");

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());

        address2.setBuildingNumber("102");

        assertNotEquals(address1, address2);
        assertNotEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    void testToString() {
        AddressDTO address = new AddressDTO("Seattle", "Pike Street", "Building E", "202");

        String expectedToString = "AddressDTO(city=Seattle, street=Pike Street, building=Building E, buildingNumber=202)";
        assertEquals(expectedToString, address.toString());

    }
}
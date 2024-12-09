package org.example.dao;

import org.example.dao.impl.InMemoryTraineeDAO;
import org.example.model.Address;
import org.example.model.Trainee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTraineeDAOTest {

    private InMemoryTraineeDAO inMemoryTraineeDAO;
    private Map<String, Trainee> traineeMap;

    @BeforeEach
    void setUp() {
        traineeMap = new HashMap<>();
        inMemoryTraineeDAO = new InMemoryTraineeDAO(traineeMap);
    }

    @Test
    void testCreate() {
        Address address = Address.builder()
                .city("New York")
                .street("5th Avenue")
                .building("Building A")
                .buildingNumber("123")
                .build();

        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        trainee.setAddress(address);
        trainee.setBirthDate(LocalDate.parse("1990-01-01"));

        inMemoryTraineeDAO.create(trainee);

        Trainee storedTrainee = traineeMap.get("1");
        assertNotNull(storedTrainee, "Trainee should be stored in the map");
        assertEquals("John.Doe", storedTrainee.getUsername(), "Username should be correctly generated");
        assertNotNull(storedTrainee.getPassword(), "Password should be generated");
        assertEquals(address, storedTrainee.getAddress(), "Address should be correctly set");
        String expectedBirthdate = trainee.getBirthDate().toString();
        String actualBirthdate = storedTrainee.getBirthDate().toString();

        assertEquals(expectedBirthdate, actualBirthdate, "Birthdate should be correctly set");
    }

    @Test
    void testUpdate() {
        Address address = Address.builder()
                .city("New York")
                .street("5th Avenue")
                .building("Building A")
                .buildingNumber("123")
                .build();

        Trainee trainee = new Trainee();
        trainee.setId(1);
        trainee.setAddress(address);
        traineeMap.put("1", trainee);

        trainee.setFirstName("Jane");
        trainee.setLastName("Doe");
        inMemoryTraineeDAO.update(trainee);

        Trainee updatedTrainee = traineeMap.get("1");
        assertNotNull(updatedTrainee, "Trainee should still be in the map after update");
        assertEquals("Jane", updatedTrainee.getFirstName(), "First name should be updated");
        assertEquals("Doe", updatedTrainee.getLastName(), "Last name should be updated");
        assertEquals(address, updatedTrainee.getAddress(), "Address should be correctly set");
    }

    @Test
    void testDelete() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        traineeMap.put("1", trainee);

        inMemoryTraineeDAO.delete(1);

        assertNull(traineeMap.get("1"), "Trainee should be removed from the map");
    }

    @Test
    void testSelectById() {
        Trainee trainee = new Trainee();
        trainee.setId(1);
        traineeMap.put("1", trainee);

        Trainee selectedTrainee = inMemoryTraineeDAO.selectById(1);
        assertNotNull(selectedTrainee, "Trainee should be retrievable by ID");
        assertEquals(trainee, selectedTrainee, "Retrieved trainee should match the stored trainee");
    }

    @Test
    void testListAll() {
        Trainee trainee1 = new Trainee();
        trainee1.setId(1);
        Trainee trainee2 = new Trainee();
        trainee2.setId(2);
        traineeMap.put("1", trainee1);
        traineeMap.put("2", trainee2);

        List<Trainee> allTrainees = inMemoryTraineeDAO.listAll();
        assertEquals(2, allTrainees.size(), "List all should return all trainees");
        assertTrue(allTrainees.contains(trainee1), "List should contain trainee1");
        assertTrue(allTrainees.contains(trainee2), "List should contain trainee2");
    }
}

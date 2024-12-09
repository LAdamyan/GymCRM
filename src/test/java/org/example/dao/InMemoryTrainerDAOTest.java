package org.example.dao;


import org.example.dao.impl.InMemoryTrainerDAO;
import org.example.model.Trainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InMemoryTrainerDAOTest {

    private InMemoryTrainerDAO inMemoryTrainerDAO;
    private Map<String, Trainer> trainerMap;

    @BeforeEach
    void setUp() {
        trainerMap = new HashMap<>();
        inMemoryTrainerDAO = new InMemoryTrainerDAO(trainerMap);
    }

    @Test
    void testCreate() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainer.setSpecialization("Fitness");

        inMemoryTrainerDAO.create(trainer);

        Trainer storedTrainer = trainerMap.get("1");
        assertNotNull(storedTrainer, "Trainer should be stored in the map");
        assertEquals("John.Doe", storedTrainer.getUsername(), "Username should be correctly generated");
        assertNotNull(storedTrainer.getPassword(), "Password should be generated");
        assertEquals("Fitness", storedTrainer.getSpecialization(), "Specialty should be correctly set");
    }

    @Test
    void testUpdate() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainerMap.put("1", trainer);

        trainer.setFirstName("Jane");
        trainer.setLastName("Smith");
        inMemoryTrainerDAO.update(trainer);

        Trainer updatedTrainer = trainerMap.get("1");
        assertNotNull(updatedTrainer, "Trainer should still be in the map after update");
        assertEquals("Jane", updatedTrainer.getFirstName(), "First name should be updated");
        assertEquals("Smith", updatedTrainer.getLastName(), "Last name should be updated");
    }

    @Test
    void testSelectById() {
        Trainer trainer = new Trainer();
        trainer.setId(1);
        trainerMap.put("1", trainer);

        Trainer selectedTrainer = inMemoryTrainerDAO.selectById(1);
        assertNotNull(selectedTrainer, "Trainer should be retrievable by ID");
        assertEquals(trainer, selectedTrainer, "Retrieved trainer should match the stored trainer");
    }
}

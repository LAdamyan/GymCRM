package org.gymCrm.hibernate.repo;

import org.gymCrm.hibernate.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingTypeRepository extends JpaRepository<TrainingType,Integer> {
    Optional<TrainingType> findByTypeName(String typeName);
}
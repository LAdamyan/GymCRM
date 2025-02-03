package org.gymCrm.hibernate.repo;

import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Integer> {

    Optional<Trainer> findByUsername(String username);

    void deleteByUsername(String username);

    @Query ( "SELECT t FROM Trainer t WHERE t.id NOT IN " +
            "(SELECT tr.id FROM Trainee tn JOIN tn.trainers tr WHERE tn.username = :traineeUsername)")
    Optional<List<Trainer>> getUnassignedTrainers(String traineeUsername);


}

package org.gymCrm.hibernate.repo;

import org.gymCrm.hibernate.model.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee,Integer> {

    Optional<Trainee> findByUsername(String username);

    void deleteByUsername(String username);

}

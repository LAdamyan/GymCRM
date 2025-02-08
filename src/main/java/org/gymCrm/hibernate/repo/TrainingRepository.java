package org.gymCrm.hibernate.repo;

import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training,Integer> {

    List<Training> findByTrainingType(TrainingType trainingType);

    @Query("SELECT t FROM Training t " +
            "JOIN t.trainee tr " +
            "JOIN t.trainingType tt " +
            "JOIN t.trainer tn " +
            "WHERE tr.username = :username " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:trainerName IS NULL OR tn.firstName = :trainerName) " +
            "AND (:trainingType IS NULL OR tt.typeName = :typeName)")
    List<Training> findTraineeTrainings(
            @Param("username") String username,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("trainerName") String trainerName,
            @Param("trainingType") String trainingType
    );

    @Query("SELECT t FROM Training t " +
            "JOIN t.trainer tr " +
            "JOIN t.trainee tn " +
            "WHERE tr.username = :username " +
            "AND (:fromDate IS NULL OR t.trainingDate >= :fromDate) " +
            "AND (:toDate IS NULL OR t.trainingDate <= :toDate) " +
            "AND (:traineeName IS NULL OR tn.firstName = :traineeName)")
    List<Training> findTrainerTrainings(
            @Param("username") String username,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("traineeName") String traineeName
    );

    @Query("SELECT DISTINCT t.trainingType.typeName FROM Training t")
    List<String> findDistinctTrainingTypes();
}

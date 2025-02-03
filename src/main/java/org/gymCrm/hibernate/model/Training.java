package org.gymCrm.hibernate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;


@Data
@Entity
@NoArgsConstructor
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    @NotNull(message = "Training name cannot be null")
    @Column(name = "training_name", nullable = false)
    private String trainingName;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_type_id",nullable = false)
    private TrainingType trainingType;

    @NotNull(message = "Training date cannot be null")
    @Column(name = "training_date")
    private Date trainingDate;

    @Column(name = "duration")
    private int duration;


    @ManyToOne
    @JoinColumn(name = "trainee_id")
    private Trainee trainee;

    @ManyToOne
    @JoinColumn(name = "trainer_id",nullable = false)
    private Trainer trainer;


    public Training(String trainingName, int id, TrainingType trainingType, Date trainingDate, Trainee trainee, Trainer trainer, int duration) {
        this.trainingName = trainingName;
        this.id = id;
        this.trainingType = trainingType;
        this.trainingDate = trainingDate;
        this.trainee = trainee;
        this.trainer = trainer;
        this.duration = duration;
    }
}
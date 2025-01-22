package org.gymCrm.hibernate.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
@Data
@Entity
@NoArgsConstructor
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;


    @Column(name = "training_name", nullable = false)
    private String trainingName;

    @NotNull(message = "Training type cannot be null")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "training_type_id")
    private TrainingType trainingType;

    @NotNull(message = "Training date cannot be null")
    @Column(name = "training_date")
    private Date trainingDate;

    @Column(name = "duration")
    private int duration;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<Trainer> trainers;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<Trainee> trainees;


    public Training(String trainingName, Date trainingDate, int duration) {
        this.trainingName = trainingName;
        this.trainingDate = trainingDate;
        this.duration = duration;
        this.trainees= new HashSet<>();
        this.trainers= new HashSet<>();
    }
}
package org.gymCrm.hibernate.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;
@Data
@Entity
@Table(name = "trainings")
public class Training {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "training_name")
    private String trainingName;

    @Column(name = "training_date")
    private Date trainingDate;

    @Column(name = "duration")
    private double duration;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<Trainer> trainers;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<Trainee> trainees;

    @OneToMany(mappedBy = "training", cascade = CascadeType.ALL)
    private Set<TrainingType> types;

}
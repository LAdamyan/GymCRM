package org.gymCrm.hibernate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainers")
@DiscriminatorValue("TRAINER")
public class Trainer extends User {

    @NotNull(message = "Specialization cannot be null")
    @Column(name = "specialization")
    private String specialization;

    @ManyToMany
    @JoinTable(
            name = "trainer_trainees",
            joinColumns = {@JoinColumn(name = "trainer_id")},
            inverseJoinColumns = {@JoinColumn(name = "trainee_id")}
    )
    private Set<Trainee> trainees;

    @NotNull(message = "Training cannot be null")
    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "training_id")
    private Training training;

}




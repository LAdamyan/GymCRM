package org.gymCrm.hibernate.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "trainees")
@DiscriminatorValue("TRAINEE")
public class Trainee extends User {

    @NotNull(message = "Birth date cannot be null")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull(message = "Address cannot be null")
    @Embedded
    @Column(name="address")
    private Address address;

    @ManyToMany(mappedBy = "trainees")
    @Column(name = "trainer_id")
    private Set<Trainer> trainers;

    @NotNull(message = "Training cannot be null")
    @ManyToOne(cascade =CascadeType. ALL)
    @JoinColumn(name = "training_id")
    private Training training;


}

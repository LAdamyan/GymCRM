package org.gymCrm.hibernate.model;

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
public class Trainee extends User {


    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Embedded
    @Column(name="address")
    private Address address;

    @ManyToMany(mappedBy = "trainees")
    @Column(name = "trainer_id")
    private Set<Trainer> trainers;

    @ManyToOne(cascade =CascadeType. ALL)
    @JoinColumn(name = "training_id")
    private Training training;


}

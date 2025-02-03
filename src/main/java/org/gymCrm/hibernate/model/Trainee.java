package org.gymCrm.hibernate.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(name = "trainees")
@DiscriminatorValue("TRAINEE")
public class Trainee extends User {

    @NotNull(message = "Birth date cannot be null")
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @NotNull(message = "Address cannot be null")

    @Embedded
    private Address address;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "training",
            joinColumns = @JoinColumn(name = "trainee_id"),
            inverseJoinColumns = @JoinColumn(name = "trainer_id")
    )
    private Set<Trainer> trainers=new HashSet<>();


    @OneToMany(mappedBy = "trainee",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Training> trainings = new HashSet<>();

}

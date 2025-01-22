package org.gymCrm.hibernate.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(name = "trainers")
@DiscriminatorValue("TRAINER")
public class Trainer extends User {

    @NotNull(message = "Specialization cannot be null")
    @Column(name = "specialization")
    private String specialization;

    @ManyToMany(fetch = FetchType.EAGER)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(this.getId(), trainer.getId());
    }
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), this.getId());
    }

}




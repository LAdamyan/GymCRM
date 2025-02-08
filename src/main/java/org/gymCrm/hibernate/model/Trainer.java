package org.gymCrm.hibernate.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(callSuper = true)
@Table(name = "trainers")
@DiscriminatorValue("TRAINER")
public class Trainer extends User {

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "training_type_id",nullable = false)
    private TrainingType specialization;

    @ManyToMany(mappedBy = "trainers", fetch = FetchType.EAGER)
    private Set<Trainee> trainees= new HashSet<>();

    @OneToMany(mappedBy = "trainer",cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Training> trainings = new HashSet<>();

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




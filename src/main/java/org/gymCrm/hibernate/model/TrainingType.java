package org.gymCrm.hibernate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="trainingTypes")
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(STRING)
    @Column(name = "type_name", unique = true)
    private Type type;

    @ManyToOne(cascade = ALL)
    @JoinColumn(name = "training_id")
    private Training training;

}

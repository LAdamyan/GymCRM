package org.gymCrm.hibernate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrainerSummaryDTO {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
}

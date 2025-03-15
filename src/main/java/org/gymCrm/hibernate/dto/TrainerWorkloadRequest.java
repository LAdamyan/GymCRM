package org.gymCrm.hibernate.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.netflix.appinfo.InstanceInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerWorkloadRequest {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean isActive;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate trainingDate;
    private int trainingDuration;


}



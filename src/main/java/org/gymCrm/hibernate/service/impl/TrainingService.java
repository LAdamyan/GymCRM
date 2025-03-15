package org.gymCrm.hibernate.service.impl;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.InstanceInfo.ActionType;
import org.gymCrm.hibernate.dto.TrainerWorkloadRequest;
import org.gymCrm.hibernate.dto.TrainingRequest;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.service.TrainerWorkloadClient;
import org.springframework.stereotype.Service;

@Service
public class TrainingService {

    private final TrainerWorkloadClient trainerWorkloadClient;

    public TrainingService(TrainerWorkloadClient trainerWorkloadClient) {
        this.trainerWorkloadClient = trainerWorkloadClient;
    }

    public void addTraining(TrainingRequest request) {
        TrainerWorkloadRequest workloadRequest = new TrainerWorkloadRequest(
                request.getTrainerUsername(),
                request.getTrainerFirstName(),
                request.getTrainerLastName(),
                request.isActive(),
                request.getTrainingDate(),
                request.getTrainingDuration()
        );
        trainerWorkloadClient.updateTrainerWorkload(workloadRequest);
    }

    public void deleteTraining(TrainingRequest request) {
        TrainerWorkloadRequest workloadRequest = new TrainerWorkloadRequest(
                request.getTrainerUsername(),
                request.getTrainerFirstName(),
                request.getTrainerLastName(),
                request.isActive(),
                request.getTrainingDate(),
                request.getTrainingDuration()
        );
        trainerWorkloadClient.updateTrainerWorkload(workloadRequest);
    }
}

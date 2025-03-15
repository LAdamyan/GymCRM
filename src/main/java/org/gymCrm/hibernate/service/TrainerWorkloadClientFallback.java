package org.gymCrm.hibernate.service;

import com.netflix.appinfo.InstanceInfo;
import org.gymCrm.hibernate.dto.TrainerWorkloadRequest;
import org.springframework.stereotype.Component;

@Component
public class TrainerWorkloadClientFallback  implements TrainerWorkloadClient{

    @Override
    public void updateTrainerWorkload(TrainerWorkloadRequest trainer) {
        System.out.println("Fallback Triggered: Trainer Workload Microservice is unavailable!");
    }

}

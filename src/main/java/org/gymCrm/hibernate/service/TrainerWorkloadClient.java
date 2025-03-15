package org.gymCrm.hibernate.service;

import com.netflix.appinfo.InstanceInfo;
import org.gymCrm.hibernate.dto.TrainerWorkloadRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "trainer-workload-service", fallback = TrainerWorkloadClientFallback.class)
public interface TrainerWorkloadClient {

    @PostMapping("/api/v1/workload")
    void updateTrainerWorkload(@RequestBody TrainerWorkloadRequest trainer);
}

package org.gymCrm.hibernate.controller;


import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.TrainingRequest;
import org.gymCrm.hibernate.service.impl.TrainingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/trainings")

public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @PostMapping("/addTraining")
    public ResponseEntity<String> addTraining(@RequestBody TrainingRequest request) {
        trainingService.addTraining(request);
        return ResponseEntity.ok("Training added successfully!");
    }

    @DeleteMapping("/deleteTraining")
    public ResponseEntity<String> deleteTraining(@RequestBody TrainingRequest request) {
        trainingService.deleteTraining(request);
        return ResponseEntity.ok("Training deleted successfully!");
    }


}

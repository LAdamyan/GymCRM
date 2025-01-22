package org.gymCrm.hibernate.controller;

import io.swagger.annotations.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.training.AddTrainingDTO;
import org.gymCrm.hibernate.dto.training.TraineeTrainingResponse;
import org.gymCrm.hibernate.dto.training.TrainingDTO;
import org.gymCrm.hibernate.dto.training.TrainingTypeDTO;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.service.TrainingService;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/trainings")
@RequiredArgsConstructor
@Api(value = "Training Management System", tags = {"Trainings"})
public class TrainingController {

    private final TrainingService trainingService;


    @ApiOperation(value = "Add a new training", tags = {"Trainings"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Training successfully created"),
            @ApiResponse(code = 403, message = "Access denied"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<?> addTraining(@RequestBody @Valid AddTrainingDTO addTrainingDTO) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}]POST /trainings called with request: {}", transactionId, addTrainingDTO);

        try {
            TrainingType trainingType = new TrainingType(addTrainingDTO.getTrainingName());
            Training training = convertToEntity(addTrainingDTO, trainingType);

            log.info("Converted AddTrainingDTO to Training entity: {}", training);

            trainingService.createTraining(training, addTrainingDTO.getTrainerUsername(), addTrainingDTO.getTraineeUsername());

            log.info("[{}]Training successfully created for trainer: {} and trainee: {}", transactionId, addTrainingDTO.getTrainerUsername(), addTrainingDTO.getTraineeUsername());
            return ResponseEntity.ok().build();

        } catch (SecurityException e) {
            log.error("[{}] Access denied while creating training: {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());

        } catch (Exception e) {
            log.error("[{}] Failed to create training due to an unexpected error: {}", transactionId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create training");
        }
    }

    private Training convertToEntity(@Valid AddTrainingDTO addTrainingDTO, TrainingType trainingType) {
        Training training = new Training();
        training.setTrainingName(addTrainingDTO.getTrainingName());
        training.setTrainingType(trainingType);
        training.setTrainingDate(addTrainingDTO.getTrainingDate());
        training.setDuration(addTrainingDTO.getDuration());
        return training;
    }

    @ApiOperation(value = "Get training for a trainee", response = ResponseEntity.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the list of trainings for the trainee"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    @GetMapping("/trainee/{username}")
    public ResponseEntity<List<TraineeTrainingResponse>> getTraineeTrainings(
            @ApiParam(value = "Username of trainee", required = true) @PathVariable String username,
            @ApiParam(value = "Start date of the training period filter  (optional)") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @ApiParam(value = "End date of the training period filter  (optional)") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @ApiParam(value = "Trainer name filter") @RequestParam(required = false) String trainerName,
            @ApiParam(value = "Training type filter") @RequestParam(required = false) TrainingType trainingType) {

        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainings/trainee/{} called with filters fromDate: {}, toDate: {}, trainerName: {}, trainingType: {}",
                transactionId, username, fromDate, toDate, trainerName, trainingType);

        Optional<List<Training>> trainingsOptional = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        log.info("[{}] Retrieved trainings: {}", transactionId, trainingsOptional);

        Optional<List<TraineeTrainingResponse>> trainingsOpt = trainingService.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType)
                .map(trainings -> trainings.stream()
                        .map(training -> new TraineeTrainingResponse(
                                training.getTrainingName(),
                                training.getTrainingDate(),
                                training.getTrainingType().getTypeName(),
                                training.getDuration(),
                                training.getTrainers().stream()
                                        .map(trainer -> trainer.getFirstName() + " " + trainer.getLastName())
                                        .collect(Collectors.joining(", "))
                        )).collect(Collectors.toList()));

        if (trainingsOpt.isPresent() && !trainingsOpt.get().isEmpty()) {
            log.info("Successfully retrieved trainings for trainee: {}", username);
            return ResponseEntity.ok(trainingsOpt.get());
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @ApiOperation(value = "Get training for a trainer", response = ResponseEntity.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved the list of trainings for the trainer", response = TrainingDTO.class, responseContainer = "List"),
            @ApiResponse(code = 204, message = "No trainings found for the trainer"),
            @ApiResponse(code = 404, message = "Trainer not found")

    })
    @GetMapping("/trainer/{username}")
    public ResponseEntity<List<TrainingDTO>> getTrainerTrainings(
            @ApiParam(value = "Username of trainer", required = true) @PathVariable String username,
            @ApiParam(value = "Start date of the training period filter (optional)") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @ApiParam(value = "End date of the training period filter (optional)") @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @ApiParam(value = "Trainee name filter (optional)") @RequestParam(required = false) String traineeName) {

        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainings/trainer/{} called with filters fromDate: {}, toDate: {}, traineeName: {}",
                transactionId, username, fromDate, toDate, traineeName);

        Optional<List<TrainingDTO>> trainingsOpt = trainingService.getTrainerTrainings(username, fromDate, toDate, traineeName)
                .map(trainings -> trainings.stream()
                        .map(training -> new TrainingDTO(
                                training.getTrainingName(),
                                training.getTrainingDate(),
                                training.getTrainingType(),
                                training.getDuration(),
                                training.getTrainees().stream()
                                        .map(trainee -> trainee.getFirstName() + " " + trainee.getLastName())
                                        .collect(Collectors.joining(", "))
                        )).collect(Collectors.toList()));

        if (trainingsOpt.isPresent() && !trainingsOpt.get().isEmpty()) {
            log.info("Successfully retrieved trainings for trainer: {}", username);
            return ResponseEntity.ok(trainingsOpt.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @ApiOperation(value = "Get all available training types", response = List.class)
    @ApiResponses({
            @ApiResponse(code = 200, message = "Successfully retrieved list of training types")
    })
    @GetMapping("/types")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainings/types called", transactionId);

        List<String> trainingTypes = trainingService.getAllTrainingTypes();

        List<TrainingTypeDTO> trainingTypeDTOs = trainingTypes.stream()
                .map(TrainingTypeDTO::new)
                .collect(Collectors.toList());
        log.info("[{}] Successfully retrieved {} training types", transactionId, trainingTypeDTOs.size());

        return ResponseEntity.ok(trainingTypeDTOs);
    }


}

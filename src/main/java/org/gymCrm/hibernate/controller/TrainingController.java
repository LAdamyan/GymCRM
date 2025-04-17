package org.gymCrm.hibernate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.TrainingRequest;
import org.gymCrm.hibernate.dto.training.AddTrainingDTO;
import org.gymCrm.hibernate.dto.training.TraineeTrainingResponse;
import org.gymCrm.hibernate.dto.training.TrainingDTO;
import org.gymCrm.hibernate.dto.training.TrainingTypeDTO;
import org.gymCrm.hibernate.model.Training;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.service.TrainingService;
import org.gymCrm.hibernate.service.impl.TrainingServiceImpl;
import org.slf4j.MDC;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j

@RestController
@RequestMapping("/trainings")
public class TrainingController {

    private final TrainingServiceImpl trainingServiceImpl;

    public TrainingController(TrainingServiceImpl trainingServiceImpl) {
        this.trainingServiceImpl = trainingServiceImpl;
    }

    @Operation(summary = "Add a new training", description = "Creates a new training record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Training successfully created"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity<?> addTraining(@RequestBody @Valid AddTrainingDTO addTrainingDTO) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}]POST /trainings called with request: {}", transactionId, addTrainingDTO);

        try {
            TrainingType trainingType = new TrainingType(addTrainingDTO.getTrainingName());
            Training training = convertToEntity(addTrainingDTO, trainingType);

            log.info("Converted AddTrainingDTO to Training entity: {}", training);

            trainingServiceImpl.create(training);

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

    @Operation(summary = "Get training for a trainee", description = "Retrieves all training records assigned to a specific trainee.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of trainings for the trainee"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/trainee/{username}")
    public ResponseEntity<List<TraineeTrainingResponse>> getTraineeTrainings(
            @PathVariable @NotBlank(message = "Username is required.") String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) String trainerName,
            @RequestParam(required = false) TrainingType trainingType) {

        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainings/trainee/{} called with filters fromDate: {}, toDate: {}, trainerName: {}, trainingType: {}",
                transactionId, username, fromDate, toDate, trainerName, trainingType);

        Optional<List<Training>> trainingsOptional = trainingServiceImpl.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType);

        log.info("[{}] Retrieved trainings: {}", transactionId, trainingsOptional);

        Optional<List<TraineeTrainingResponse>> trainingsOpt = trainingServiceImpl.getTraineeTrainings(username, fromDate, toDate, trainerName, trainingType)
                .map(trainings -> trainings.stream()
                        .map(training -> new TraineeTrainingResponse(
                                training.getTrainingName(),
                                training.getTrainingDate(),
                                training.getTrainingType().getTypeName(),
                                training.getDuration(),
                                training.getTrainer() != null ? training.getTrainer().getFirstName() + " " + training.getTrainer().getLastName()
                                        : "No trainer assigned")).collect(Collectors.toList()));

        if (trainingsOpt.isPresent() && !trainingsOpt.get().isEmpty()) {
            log.info("Successfully retrieved trainings for trainee: {}", username);
            return ResponseEntity.ok(trainingsOpt.get());
        } else {
            return ResponseEntity.noContent().build();
        }

    }

    @Operation(summary = "Get training for a trainer", description = "Retrieves all training records assigned to a specific trainer.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the training list for the trainer"),
            @ApiResponse(responseCode = "204", description = "No trainings found for the trainer"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/trainer/{username}")
    public ResponseEntity<List<TrainingDTO>> getTrainerTrainings(
            @PathVariable @NotBlank(message = "Username is required.") String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date fromDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date toDate,
            @RequestParam(required = false) String traineeName) {

        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainings/trainer/{} called with filters fromDate: {}, toDate: {}, traineeName: {}",
                transactionId, username, fromDate, toDate, traineeName);

        Optional<List<TrainingDTO>> trainingsOpt = trainingServiceImpl.getTrainerTrainings(username, fromDate, toDate, traineeName)
                .map(trainings -> trainings.stream()
                        .map(training -> new TrainingDTO(
                                training.getTrainingName(),
                                training.getTrainingDate(),
                                training.getTrainingType(),
                                training.getDuration(),
                                training.getTrainee() != null ? training.getTrainee().getFirstName() + " " + training.getTrainee().getLastName()
                                        : "No trainee assigned")).collect(Collectors.toList()));

        if (trainingsOpt.isPresent() && !trainingsOpt.get().isEmpty()) {
            log.info("Successfully retrieved trainings for trainer: {}", username);
            return ResponseEntity.ok(trainingsOpt.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Get all available training types", description = "Retrieves a list of all available training types.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of training types")
    })
    @GetMapping("/types")
    public ResponseEntity<List<TrainingTypeDTO>> getTrainingTypes() {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainings/types called", transactionId);

        List<String> trainingTypes = trainingServiceImpl.getDistinctTrainingTypes();

        List<TrainingTypeDTO> trainingTypeDTOs = trainingTypes.stream()
                .map(TrainingTypeDTO::new)
                .collect(Collectors.toList());
        log.info("[{}] Successfully retrieved {} training types", transactionId, trainingTypeDTOs.size());

        return ResponseEntity.ok(trainingTypeDTOs);
    }

    @PostMapping("/addTraining")
    public ResponseEntity<String> addTraining(@RequestBody TrainingRequest request) {
        trainingServiceImpl.addTraining(request);
        return ResponseEntity.ok("Training added successfully!");
    }

    @DeleteMapping("/deleteTraining")
    public ResponseEntity<String> deleteTraining(@RequestBody TrainingRequest request) {
        trainingServiceImpl.deleteTraining(request);
        return ResponseEntity.ok("Training deleted successfully!");
    }


}
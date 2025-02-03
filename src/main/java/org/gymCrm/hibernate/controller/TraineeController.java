package org.gymCrm.hibernate.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.address.AddressDTO;
import org.gymCrm.hibernate.dto.trainee.TraineeDTO;
import org.gymCrm.hibernate.dto.trainee.TraineeProfileDTO;
import org.gymCrm.hibernate.dto.trainee.UpdateTraineeTrainersDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerDTO;
import org.gymCrm.hibernate.endpoint.CustomMetrics;
import org.gymCrm.hibernate.model.Address;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.repo.TrainerRepository;
import org.gymCrm.hibernate.service.impl.TraineeServiceImpl;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
public class TraineeController {

    private final TraineeServiceImpl traineeServiceImpl;
    private final UserDetailsService<Trainee> userDetailsService;
    private final TrainerRepository trainerRepository;

    private void authenticate(String username, String password) {
        log.info("Authenticating user: {}", username);
        if (!userDetailsService.authenticate(username, password)) {
            log.error("Authentication failed for user: {}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        log.info("Authentication successful for user: {}", username);
    }

    @Operation(summary = "Register a new trainee", description = "Registers a new trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Map<String, String>> registerTrainee(@RequestBody @Valid TraineeDTO traineeDTO) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] POST /trainees/register called with request: {}", transactionId, traineeDTO);
        Address address = null;
        if (traineeDTO.getAddress() != null) {
            AddressDTO addressDTO = traineeDTO.getAddress();
            address = new Address();
            address.setCity(addressDTO.getCity());
            address.setStreet(addressDTO.getStreet());
            address.setBuilding(addressDTO.getBuilding());
            address.setBuildingNumber(addressDTO.getBuildingNumber());
        }

        Trainee trainee = new Trainee();
        trainee.setFirstName(traineeDTO.getFirstName());
        trainee.setLastName(traineeDTO.getLastName());
        trainee.setBirthDate(traineeDTO.getBirthDate());
        trainee.setAddress(address);
        trainee.setActive(true);

        try {

            Map<String, String> response = new HashMap<>();
            response.put("username", trainee.getUsername());
            response.put("password", trainee.getPassword());
            log.info("[{}] Trainee registered successfully with username: {}", transactionId, trainee.getUsername());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[{}] Error registering trainee: {}", transactionId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", "Registration failed"));
        }
    }

    @Operation(summary = "Get trainee profile", description = "Retrieves the profile of a trainee by their username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDTO> getTraineeProfile(
            @PathVariable @NotBlank(message = "Username is required.") String username,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword) {

        authenticate(authUsername, authPassword);
        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainees/{} called", transactionId, username);

        Optional<Trainee> traineeOptional = traineeServiceImpl.selectByUsername(username);

        if (traineeOptional.isEmpty()) {
            throw new EntityNotFoundException("Trainee with username " + username + " not found");
        }
        log.info("[{}] Trainee profile retrieved successfully for username: {}", transactionId, username);
        return ResponseEntity.ok(new TraineeProfileDTO(traineeOptional.get()));
    }

    @Operation(summary = "Delete trainee profile", description = "Deletes the profile of a trainee by their username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee profile deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(
            @PathVariable String username,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] DELETE /trainees/{} called", transactionId, username);

        authenticate(authUsername, authPassword);

        traineeServiceImpl.delete(username);
        log.info("[{}] Trainee with username: {} deleted successfully", transactionId, username);
        return ResponseEntity.ok().build();

    }

    @Operation(summary = "Change trainee active status", description = "Updates the active status of a trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found")
    })
    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> changeTraineeStatus(
            @PathVariable String username,
            @RequestParam String password,
            @RequestParam boolean isActive,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword) {
        String transactionId = MDC.get("transactionId");
        authenticate(authUsername, authPassword);

        log.info("[{}] PATCH /trainees/{}/status called with isActive: {}", transactionId, username);

        traineeServiceImpl.changeTraineeActiveStatus(username, password,isActive);
        log.info("[{}] Trainee status changed to {} for username: {}", transactionId, username);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Update trainee's trainer list", description = "Updates the trainers assigned to a trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainee's trainer list updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{traineeUsername}/trainers")
    public ResponseEntity<List<TrainerDTO>> updateTraineeTrainers(
            @PathVariable String traineeUsername,
            @RequestBody @Valid UpdateTraineeTrainersDTO requestDTO
           ) {

        String transactionId = MDC.get("transactionId");
        log.info("[{}] PUT /trainees/{}/trainers", transactionId, traineeUsername);

        try {
            List<Trainer> trainersToUpdate = requestDTO.getTrainerUsernames().stream()
                    .map(trainerUsername -> trainerRepository.findByUsername(trainerUsername)
                            .orElseThrow(() -> new EntityNotFoundException("Trainer with username " + trainerUsername + " not found")))
                    .collect(Collectors.toList());

            traineeServiceImpl.updateTraineeTrainers(traineeUsername, trainersToUpdate);

            Optional<Trainee> traineeOpt = traineeServiceImpl.selectByUsername(traineeUsername);
            if (traineeOpt.isPresent()) {
                Trainee trainee = traineeOpt.get();
                List<TrainerDTO> trainerDTOs = Optional.ofNullable(trainee.getTrainers())
                        .orElse(new HashSet<>())
                        .stream()
                        .map(this::convertToTrainerDTO)
                        .collect(Collectors.toList());
                log.info("Trainers updated successfully for trainee: {}", traineeUsername);
                return ResponseEntity.ok(trainerDTOs);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("[{}] Update trainers failed: {}", transactionId, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    private TrainerDTO convertToTrainerDTO(Trainer trainer) {
        return new TrainerDTO(
                trainer.getUsername(),
                trainer.getFirstName(),
                trainer.getLastName(),
                new TrainingType(
                        trainer.getSpecialization().getTypeName())
        );
    }

}

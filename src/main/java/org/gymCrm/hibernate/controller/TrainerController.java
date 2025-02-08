package org.gymCrm.hibernate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.trainer.ActiveTrainerDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerDTO;
import org.gymCrm.hibernate.dto.trainer.TrainerProfileDTO;
import org.gymCrm.hibernate.dto.trainer.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.model.TrainingType;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.gymCrm.hibernate.service.impl.TrainerServiceImpl;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class TrainerController {


    private final TrainerServiceImpl trainerServiceImpl;
    private final AuthenticationService  authenticationService;

    private void authenticate(String username, String password) {
        if (!authenticationService.authenticate(username, password)) {
            log.error("Authentication failed for user: {}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        log.info("Authentication successful for user: {}", username);
    }

    @GetMapping("/{username}/exists")
    public ResponseEntity<String> checkTrainerExists(@PathVariable String username) {
        boolean exists = trainerServiceImpl.doesTrainerExist(username);
        if (exists) {
            return ResponseEntity.ok("Trainer exists.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trainer not found.");
        }
    }

    @Operation(summary = "Register a new trainer", description = "Registers a new trainer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer successfully registered"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    public ResponseEntity<Map<String, String>> registerTrainer(@RequestBody @Valid TrainerDTO trainerDTO) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] POST /trainers/register called with request: {}", transactionId, trainerDTO);

        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerDTO.getFirstName());
        trainer.setLastName(trainerDTO.getLastName());

        if (trainerDTO.getSpecialization() != null) {
            trainer.setSpecialization(trainerDTO.getSpecialization());
        }

        log.info("Trainer after creation: {}", trainer);

        Map<String, String> response = trainerServiceImpl.create(trainer);
        log.info("[{}] Trainer registered successfully with username: {}", transactionId, trainer.getUsername());
        return ResponseEntity.ok(response);

    }

    @Operation(summary = "Get trainer profile", description = "Retrieves the profile of a trainer by their username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TrainerProfileDTO> getTrainerProfile(
            @PathVariable String username) {

        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainers/{} called", transactionId, username);

        Optional<Trainer> trainer = trainerServiceImpl.selectByUsername(username);
        return trainer.map(t -> ResponseEntity.ok(new TrainerProfileDTO(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Update trainer profile", description = "Updates the profile of a trainer by their username.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer profile updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @PutMapping("/{username}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<TrainerProfileDTO> updateTrainerProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateTrainerDTO updateDTO) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] PUT /trainers/{} called with update data: {}", transactionId, username, updateDTO);


        Optional<Trainer> updatedTrainerOpt = trainerServiceImpl.updateTrainer(username, updateDTO);

        if (updatedTrainerOpt.isPresent()) {
            log.info("[{}] Trainer profile updated successfully for username: {}", transactionId, username);
            return ResponseEntity.ok(new TrainerProfileDTO(updatedTrainerOpt.get()));
        }
        log.warn("[{}] Trainer not found for username: {}", transactionId, username);
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Change trainer active status", description = "Updates the active status of a trainer.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Trainer status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Trainer not found")
    })
    @PatchMapping("/{username}/status")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Void> changeTrainerStatus(
            @PathVariable String username,
            @RequestParam String password,
            @RequestParam boolean isActive) {


        String transactionId = MDC.get("transactionId");
        log.info("[{}] PATCH /trainers/{}/status called with isActive: {}", transactionId, username);

        trainerServiceImpl.changeTrainerActiveStatus(username, password, isActive);
        log.info("[{}] Trainer status changed to {} for username: {}", transactionId, username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get not assigned active trainers for a trainee", description = "Retrieves trainers not assigned to the specified trainee.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of unassigned active trainers"),
            @ApiResponse(responseCode = "404", description = "Trainee not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{username}/not-assigned-trainers")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<ActiveTrainerDTO>> getUnassignedActiveTrainers(
            @PathVariable String username) {

        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainees/{}/not-assigned-trainers called", transactionId, username);

        Optional<List<Trainer>> trainersOptional = trainerServiceImpl.getUnassignedTrainers(username);

        if (trainersOptional.isEmpty() || trainersOptional.get().isEmpty()) {
            log.warn("[{}] No unassigned trainers found for trainee: {}", transactionId, username);
            return ResponseEntity.ok(Collections.emptyList());
        }
        List<ActiveTrainerDTO> activeTrainerDTOs = trainersOptional.get().stream()
                .map(trainer -> new ActiveTrainerDTO(
                        trainer.getUsername(),
                        trainer.getFirstName(),
                        trainer.getLastName(),
                        new TrainingType(trainer.getSpecialization().getTypeName()))
                ).toList();
        log.info("[{}] Successfully retrieved unassigned active trainers for trainee: {}", transactionId, username);
        return ResponseEntity.ok(activeTrainerDTOs);
    }
}


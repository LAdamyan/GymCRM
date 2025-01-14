package org.gymCrm.hibernate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.TrainerDTO;
import org.gymCrm.hibernate.dto.TrainerProfileDTO;
import org.gymCrm.hibernate.dto.UpdateTrainerDTO;
import org.gymCrm.hibernate.model.Trainer;
import org.gymCrm.hibernate.service.TrainerService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/trainers")
@RequiredArgsConstructor
@Api(value = "Trainer Management System", tags = {"Trainers"})
public class TrainerController {

    private final TrainerService trainerService;
    private final UserDetailsService<Trainer> userDetailsService;

    private void authenticate(String username,String password){
        if(!userDetailsService.authenticate(username,password)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Invalid username or password");
        }
    }

    @ApiOperation(value = "Register a new trainer", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer successfully registered"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    @PostMapping("/register")
    public ResponseEntity<Map<String,String>>registerTrainer(@RequestBody @Valid TrainerDTO trainerDTO){
        String transactionId = MDC.get("transactionId");
        log.info("[{}] POST /trainers/register called with request: {}",transactionId,trainerDTO);
        Trainer trainer = new Trainer();
        trainer.setFirstName(trainerDTO.getFirstName());
        trainer.setLastName(trainerDTO.getLastName());
        trainer.setSpecialization(trainerDTO.getSpecialization());
        trainer.setActive(true);

        trainerService.saveTrainer(trainer);

        Map<String, String> response = new HashMap<>();
        response.put("username", trainer.getUsername());
        response.put("password", trainer.getPassword());
        log.info("[{}] Trainer registered successfully with username: {}",transactionId, trainer.getUsername());
        return ResponseEntity.ok(response);

    }

    @ApiOperation(value = "Get trainer profile", response = TrainerProfileDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer profile retrieved successfully"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<TrainerProfileDTO>getTrainerProfile(
            @PathVariable String username,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword){
        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainers/{} called",transactionId, username);
        authenticate(authUsername,authPassword);

        Optional<Trainer>optionalTrainer = trainerService.getTrainerByUsername(username);
        return optionalTrainer.map(trainer -> ResponseEntity.ok
                (new TrainerProfileDTO(trainer))).orElseGet(() ->
                ResponseEntity.notFound().build());
    }

    @ApiOperation(value = "Update trainer profile", response = TrainerProfileDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer profile updated successfully"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    @PutMapping("/{username}")
    public ResponseEntity<TrainerProfileDTO> updateTrainerProfile(
            @PathVariable String username,
            @RequestBody @Valid UpdateTrainerDTO updateDTO,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] PUT /trainers/{} called with update data: {}",transactionId, username, updateDTO);

        authenticate(authUsername,authPassword);

        Optional<Trainer> updatedTrainerOpt = trainerService.updateTrainer(username, updateDTO);

        if (updatedTrainerOpt.isPresent()) {
            log.info("[{}] Trainer profile updated successfully for username: {}",transactionId, username);
            return ResponseEntity.ok(new TrainerProfileDTO(updatedTrainerOpt.get()));
        }
        log.warn("[{}] Trainer not found for username: {}",transactionId, username);
        return ResponseEntity.notFound().build();
    }
    @ApiOperation(value = "Change trainer active status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainer status updated successfully"),
            @ApiResponse(code = 404, message = "Trainer not found")
    })
    @PatchMapping("/{username}/status")
    public ResponseEntity<Void> changeTrainerStatus(
            @PathVariable String username,
            @RequestParam String password,
            @RequestParam boolean isActive,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword) {


        authenticate(authUsername,authPassword);
        String transactionId = MDC.get("transactionId");
        log.info("[{}] PATCH /trainers/{}/status called with isActive: {}",transactionId, username, isActive);

        trainerService.changeTrainerActiveStatus(username,password,isActive);
        log.info("[{}] Trainer status changed to {} for username: {}",transactionId, isActive, username);
        return ResponseEntity.ok().build();
    }

    }

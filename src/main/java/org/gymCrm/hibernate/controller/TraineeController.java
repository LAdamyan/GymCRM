package org.gymCrm.hibernate.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dto.AddressDTO;
import org.gymCrm.hibernate.dto.TraineeDTO;
import org.gymCrm.hibernate.dto.TraineeProfileDTO;
import org.gymCrm.hibernate.model.Address;
import org.gymCrm.hibernate.model.Trainee;
import org.gymCrm.hibernate.service.TraineeService;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/trainees")
@RequiredArgsConstructor
@Api(value = "Trainee Management System", tags = {"Trainees"})
public class TraineeController {

    private final TraineeService traineeService;
    private final UserDetailsService<Trainee> userDetailsService;

    private void authenticate(String username, String password) {
        log.info("Authenticating user: {}", username);
        if (!userDetailsService.authenticate(username, password)) {
            log.error("Authentication failed for user: {}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        log.info("Authentication successful for user: {}", username);
    }

    @ApiOperation(value = "Register a new trainee", response = Map.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee successfully registered"),
            @ApiResponse(code = 400, message = "Invalid input")
    })
    @PostMapping("/register")
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

        traineeService.saveTrainee(trainee);

        Map<String, String> response = new HashMap<>();
        response.put("username", trainee.getUsername());
        response.put("password", trainee.getPassword());
        log.info("[{}] Trainee registered successfully with username: {}", transactionId, trainee.getUsername());
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Get trainee profile", response = TraineeProfileDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee profile retrieved successfully"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    @GetMapping("/{username}")
    public ResponseEntity<TraineeProfileDTO> getTraineeProfile(
            @PathVariable @NotBlank(message = "Username is required.") String username,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword) {

        authenticate(authUsername, authPassword);
        String transactionId = MDC.get("transactionId");
        log.info("[{}] GET /trainees/{} called", transactionId, username);

        Optional<Trainee> traineeOptional = traineeService.getTraineeByUsername(username);

        if (traineeOptional.isEmpty()) {
            throw new EntityNotFoundException("Trainee with username " + username + " not found");
        }
        log.info("[{}] Trainee profile retrieved successfully for username: {}", transactionId, username);
        return ResponseEntity.ok(new TraineeProfileDTO(traineeOptional.get()));
    }

    @ApiOperation(value = "Delete trainee profile")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee profile deleted successfully"),
            @ApiResponse(code = 404, message = "Trainee not found")
    })
    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteTrainee(
            @PathVariable String username,
            @RequestHeader("X-Auth-Username") String authUsername,
            @RequestHeader("X-Auth-Password") String authPassword) {
        String transactionId = MDC.get("transactionId");
        log.info("[{}] DELETE /trainees/{} called", transactionId, username);

        authenticate(authUsername, authPassword);

        traineeService.deleteTrainee(username);
        log.info("[{}] Trainee with username: {} deleted successfully", transactionId, username);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "Change trainee active status")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Trainee status updated successfully"),
            @ApiResponse(code = 404, message = "Trainee not found")
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

        log.info("[{}] PATCH /trainees/{}/status called with isActive: {}", transactionId, username, isActive);

        traineeService.changeTraineeActiveStatus(username, password, isActive);
        log.info("[{}] Trainee status changed to {} for username: {}", transactionId, isActive, username);

        return ResponseEntity.ok().build();
    }
}

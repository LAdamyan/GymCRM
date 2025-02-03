package org.gymCrm.hibernate.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.repo.UserRepository;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.gymCrm.hibernate.service.impl.TraineeServiceImpl;
import org.gymCrm.hibernate.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequestMapping("/login")
@RestController
public class LoginController {

    private final UserDetailsService userDetailsService;

    private final UserServiceImpl userServiceImpl;

    public LoginController(UserDetailsService userDetailsService, UserServiceImpl userServiceImpl) {
        this.userDetailsService = userDetailsService;
        this.userServiceImpl = userServiceImpl;

    }

    @Operation(summary = "Login with username and password", description = "Authenticate a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @GetMapping
    public ResponseEntity<String> login(
            @RequestParam @NotBlank(message = "Username is required") String username,
            @RequestParam @NotBlank(message = "Password is required") String password) {

        log.info(" Attempting login for user: {}", username);
        if (userDetailsService.authenticate(username, password)) {
            log.info("Login successful for user: {}", username);
            return ResponseEntity.ok("200 OK");
        } else {
            log.error("Login failed for user: {}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
    }

    @Operation(summary = "Change login credentials", description = "Change the user's password.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid old password"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestParam @NotBlank(message = "Username is required") String username,
            @RequestParam @NotBlank(message = "Old password is required") String oldPassword,
            @RequestParam @NotBlank(message = "New password is required") String newPassword) {

        if (!userDetailsService.authenticate(username, oldPassword)) {
            log.error("Password change failed for user: {} - Invalid old password", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid old password");
        }
        Optional<User> user = userServiceImpl.findByUsername(username);
        if (user.isPresent()) {
            userServiceImpl.changePassword(username, newPassword);
            log.info("Password changed successfully for user: {}", username);
            return ResponseEntity.ok("Password changed successfully");
        } else {
            log.error("User not found: {}", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}

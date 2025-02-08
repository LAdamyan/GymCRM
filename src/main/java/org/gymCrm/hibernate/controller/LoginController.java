package org.gymCrm.hibernate.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.config.jwt.BruteForceProtectionService;
import org.gymCrm.hibernate.config.jwt.JwtUtil;
import org.gymCrm.hibernate.dto.AuthResponse;
import org.gymCrm.hibernate.dto.LoginRequest;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.util.AuthenticationService;
import org.gymCrm.hibernate.service.impl.UserServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@SecurityRequirement(name = "BearerAuth")
@RestController
public class LoginController {
    private final BruteForceProtectionService bruteForceProtectionService;
    private final AuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;


    private final UserServiceImpl userServiceImpl;

    public LoginController(BruteForceProtectionService bruteForceProtectionService, AuthenticationService authenticationService, AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserServiceImpl userServiceImpl) {
        this.bruteForceProtectionService = bruteForceProtectionService;
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userServiceImpl = userServiceImpl;

    }
    @SecurityRequirement(name = "")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "User Login", notes = "Endpoint for user authentication")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully authenticated"),
            @ApiResponse(responseCode = "401", description = "Unauthorized access -invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Bad request - Missing or invalid authentication fields")})
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        String username = request.getUsername();

        if (bruteForceProtectionService.isBlocked(username)) {
            return ResponseEntity.status(HttpStatus.LOCKED)
                    .body("User is temporarily blocked due to multiple failed login attempts.");
        }
        try {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
            bruteForceProtectionService.loginSucceeded(username);

            String token = jwtUtil.generateToken(username, authentication.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException e) {
            bruteForceProtectionService.loginFailed(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }

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
        if (authenticationService.authenticate(username, password)) {
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

        if (!authenticationService.authenticate(username, oldPassword)) {
            log.error("Password change failed for user: {} - Invalid old password", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid old password");
        }
        Optional<User> user = userServiceImpl.findByUsername(username);
        if (user.isPresent()) {
            userServiceImpl.changePassword(username, oldPassword, newPassword);
            log.info("Password changed successfully for user: {}", username);
            return ResponseEntity.ok("Password changed successfully");
        } else {
            log.error("User not found: {}", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}

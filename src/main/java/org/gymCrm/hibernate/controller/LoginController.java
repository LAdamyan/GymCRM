package org.gymCrm.hibernate.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
import org.gymCrm.hibernate.dao.UserDAO;
import org.gymCrm.hibernate.model.User;
import org.gymCrm.hibernate.service.UserDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@RequestMapping("/login")
@RestController
@Api(value = "Login Management System", tags = {"Login"})
public class LoginController {

    private final UserDetailsService userDetailsService;
    private final UserDAO userDAO;


    public LoginController(UserDetailsService userDetailsService, UserDAO userDAO) {
        this.userDetailsService = userDetailsService;
        this.userDAO = userDAO;
    }
    @ApiOperation(value = "Login with username and password", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Login successful"),
            @ApiResponse(code = 401, message = "Invalid username or password")
    })
    @GetMapping
    public ResponseEntity<String> login(
            @RequestParam @NotBlank(message = "Username is required") String username,
            @RequestParam @NotBlank(message = "Password is required") String password) {

        log.info(" Attempting login for user: {}", username);
        if(userDetailsService.authenticate(username,password)){
            log.info("Login successful for user: {}", username);
            return ResponseEntity.ok("200 OK");
    }else{
            log.error("Login failed for user: {}", username);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        }

    @ApiOperation(value = "Change login credentials", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password changed successfully"),
            @ApiResponse(code = 401, message = "Invalid old password")
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
        Optional<User> user = userDAO.findByUsername(username);
        if (user.isPresent()) {
            userDAO.changePassword(username, newPassword);
            log.info("Password changed successfully for user: {}", username);
            return ResponseEntity.ok("Password changed successfully");
        } else {
            log.error("User not found: {}", username);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

}

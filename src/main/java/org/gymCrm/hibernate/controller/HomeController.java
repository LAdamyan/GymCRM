package org.gymCrm.hibernate.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Welcome to the Gym CRM API! Use /swagger-ui.html to view the API documentation.";
    }
}
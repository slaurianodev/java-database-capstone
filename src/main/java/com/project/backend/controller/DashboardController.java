package com.project.backend.controller;

/**
 * Created by Sergio.
 */

import com.project.backend.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private TokenService tokenService;

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        boolean validationResult = tokenService.validateToken(token, "admin");

        if (validationResult) {
            // Token is valid
            return "admin/adminDashboard";
        } else {
            // Token is invalid
            return "redirect:http://localhost:8080";
        }
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        boolean validationResult = tokenService.validateToken(token, "doctor");

        if (validationResult) {
            // Token is valid
            return "doctor/doctorDashboard";
        } else {
            // Token is invalid
            return "redirect:http://localhost:8080";
        }
    }
}

package com.project.backend.controller;

/**
 * Created by Sergio.
 */
import com.project.backend.models.Admin;
import com.project.backend.service.MainService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}" + "admin")
public class AdminController {

    private final MainService mainService;

    public AdminController(MainService mainService) {
        this.mainService = mainService;
    }

    // =========================
    // ADMIN LOGIN
    // =========================

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        return mainService.validateAdmin(admin);
    }
}

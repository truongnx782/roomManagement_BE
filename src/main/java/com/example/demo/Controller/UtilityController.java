package com.example.demo.controller;

import com.example.demo.dto.UtilityDTO;
import com.example.demo.service.UtilityService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/utilitie")
public class UtilityController {
    private final UtilityService utilityService;

    public UtilityController(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, Object> payload) {
        try {
            Page<UtilityDTO> result = utilityService.search(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search for utilities: " + e.getMessage());
        }
    }
}

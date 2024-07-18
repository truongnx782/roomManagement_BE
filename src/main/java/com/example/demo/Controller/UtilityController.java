package com.example.demo.Controller;

import com.example.demo.Service.UtilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/utility")
public class UtilityController {
    private final UtilityService utilityService;

    public UtilityController(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(utilityService.search(payload));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search for utilities: " + e.getMessage());
        }
    }


}

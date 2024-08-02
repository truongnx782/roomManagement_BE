package com.example.demo.Controller;

import com.example.demo.DTO.MaintenanceDTO;
import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Service.MaintenanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/maintenance")
@CrossOrigin(origins = "*",maxAge = 3600)

public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }


    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(maintenanceService.search(payload));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for maintenance: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody MaintenanceDTO maintenanceDTO) {
        try {
            return ResponseEntity.ok(maintenanceService.create(maintenanceDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create maintenance: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(maintenanceService.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve maintenance: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@PathVariable("id") BigInteger id,
                                    @RequestBody MaintenanceDTO maintenanceDTO) {
        try {
            return ResponseEntity.ok(maintenanceService.update(id, maintenanceDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create maintenance: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(maintenanceService.delete(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete maintenance: " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(maintenanceService.restore(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to restore maintenance: " + e.getMessage());
        }
    }
}

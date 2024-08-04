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
    public ResponseEntity<?> search(@RequestHeader("cid")BigInteger cid,
                                    @RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(maintenanceService.search(payload,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for maintenance: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid")BigInteger cid,
                                    @RequestBody MaintenanceDTO maintenanceDTO) {
        try {
            return ResponseEntity.ok(maintenanceService.create(maintenanceDTO,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create maintenance: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid")BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(maintenanceService.findById(id,cid));
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
    public ResponseEntity<?> delete(@RequestHeader("cid")BigInteger cid,
                                    @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(maintenanceService.delete(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete maintenance: " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid")BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(maintenanceService.restore(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to restore maintenance: " + e.getMessage());
        }
    }
}

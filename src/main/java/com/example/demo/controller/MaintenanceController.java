package com.example.demo.controller;

import com.example.demo.DTO.MaintenanceDTO;
import com.example.demo.service.MaintenanceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/maintenance")
public class MaintenanceController {
    private final MaintenanceService maintenanceService;

    public MaintenanceController(MaintenanceService maintenanceService) {
        this.maintenanceService = maintenanceService;
    }


    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(maintenanceService.search(payload, cid));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") Long cid,
                                    @RequestBody MaintenanceDTO maintenanceDTO) {
        return ResponseEntity.ok(maintenanceService.create(maintenanceDTO, cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(maintenanceService.findById(id, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@PathVariable("id") Long id,
                                    @RequestBody MaintenanceDTO maintenanceDTO) {
        return ResponseEntity.ok(maintenanceService.update(id, maintenanceDTO));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id) {
        return ResponseEntity.ok(maintenanceService.delete(id, cid));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(maintenanceService.restore(id, cid));
    }
}

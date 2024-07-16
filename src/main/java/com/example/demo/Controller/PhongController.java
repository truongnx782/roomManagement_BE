package com.example.demo.Controller;

import com.example.demo.DTO.PhongDTO;
import com.example.demo.Service.PhongService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("phong")
public class PhongController {
    private final PhongService phongService;

    public PhongController(PhongService phongService) {
        this.phongService = phongService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchPhong(@RequestBody Map<String, Object> payload) {
        try {
            Page<PhongDTO> result = phongService.search(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search for Phong: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") BigInteger id) {
        try {
            Optional<PhongDTO> phong = phongService.findById(id);
            return ResponseEntity.ok(phong);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve Phong: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody PhongDTO phongDTO) {
        try {
            PhongDTO newPhong = phongService.create(phongDTO);
            return ResponseEntity.ok(newPhong);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create Phong: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") BigInteger id, @RequestBody PhongDTO phongDTO) {
        try {
            PhongDTO existingPhong = phongService.update(id, phongDTO);
            return ResponseEntity.ok(existingPhong);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Phong: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        try {
            PhongDTO existingPhong = phongService.delete(id);
            return ResponseEntity.ok(existingPhong);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete Phong: " + e.getMessage());
        }
    }
}


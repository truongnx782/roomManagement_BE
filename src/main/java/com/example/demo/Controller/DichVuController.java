package com.example.demo.Controller;

import com.example.demo.DTO.DichVuDTO;
import com.example.demo.Entity.DichVu;
import com.example.demo.Service.DichVuService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("dich-vu")
public class DichVuController {
    private final DichVuService dichVuService;

    public DichVuController(DichVuService dichVuService) {
        this.dichVuService = dichVuService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchDichVu(@RequestBody Map<String, Object> payload) {
        try {
            Page<DichVuDTO> result = dichVuService.searchDichVu(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search for DichVu: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") BigInteger id) {
        try {
            Optional<DichVuDTO> dichVu = dichVuService.findById(id);
            return ResponseEntity.ok(dichVu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to retrieve DichVu: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody DichVuDTO dichVuDTO) {
        try {
            DichVuDTO newDichVu = dichVuService.createDichVu(dichVuDTO);
            return ResponseEntity.ok(newDichVu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create DichVu: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") BigInteger id, @RequestBody DichVuDTO updatedDichVuDTO) {
        try {
            DichVuDTO existingDichVu = dichVuService.updateDichVu(id, updatedDichVuDTO);
            return ResponseEntity.ok(existingDichVu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update DichVu: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        try {
            DichVuDTO existingDichVu = dichVuService.deleteDichVu(id);
            return ResponseEntity.ok(existingDichVu);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to delete DichVu: " + e.getMessage());
        }
    }
}

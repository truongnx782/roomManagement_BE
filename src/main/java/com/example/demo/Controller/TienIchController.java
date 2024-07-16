package com.example.demo.Controller;

import com.example.demo.DTO.PhongDTO;
import com.example.demo.DTO.TienIchDTO;
import com.example.demo.Service.TienIchService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("tien-ich")
public class TienIchController {
    private  final TienIchService tienIchService;

    public TienIchController(TienIchService tienIchService) {
        this.tienIchService = tienIchService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, Object> payload) {
        try {
            Page<TienIchDTO> result = tienIchService.search(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to search for TienIch: " + e.getMessage());
        }
    }
}

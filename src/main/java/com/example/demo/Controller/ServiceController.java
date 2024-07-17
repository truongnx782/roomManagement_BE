package com.example.demo.Controller;

import com.example.demo.DTO.DichVuDTO;
import com.example.demo.DTO.ServiceDTO;
import com.example.demo.Service.ServiceService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/services")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchService(@RequestBody Map<String, Object> payload) {
        try {
            Page<ServiceDTO> result = serviceService.searchService(payload);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for Service: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") BigInteger id) {
        try {
            Optional<ServiceDTO> service = serviceService.findById(id);
            return service.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Service: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody ServiceDTO serviceDTO) {
        try {
            ServiceDTO newService = serviceService.createService(serviceDTO);
            return ResponseEntity.ok(newService);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Service: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") BigInteger id,
                                    @RequestBody ServiceDTO updatedServiceDTO) {
        try {
            ServiceDTO existingService = serviceService.updateService(id, updatedServiceDTO);
            return ResponseEntity.ok(existingService);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update Service: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        try {
            ServiceDTO deletedService = serviceService.deleteService(id);
            return ResponseEntity.ok(deletedService);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete Service: " + e.getMessage());
        }
    }
}

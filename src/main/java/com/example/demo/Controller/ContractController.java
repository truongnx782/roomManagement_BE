package com.example.demo.Controller;

import com.example.demo.DTO.ContractDTO;
import com.example.demo.Service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/contract")
public class ContractController {
    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(contractService.search(payload));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for contract: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody ContractDTO contractDTO) {
        try {
            return ResponseEntity.ok(contractService.create(contractDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create contract: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(contractService.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve contract: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") BigInteger id,
                                    @RequestBody ContractDTO contractDTO) {
        try {
            return ResponseEntity.ok(contractService.update(id, contractDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update contract: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(contractService.delete(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete contract: " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(contractService.restore(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to restore contract: " + e.getMessage());
        }
    }
}

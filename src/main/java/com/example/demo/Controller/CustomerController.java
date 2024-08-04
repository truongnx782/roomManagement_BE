package com.example.demo.Controller;

import com.example.demo.DTO.CustomerDTO;
import com.example.demo.Service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private  final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid")BigInteger cid,
                                    @RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(customerService.search(payload,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for customer: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid")BigInteger cid) {
        try {
            return ResponseEntity.ok(customerService.getAll(cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for customer: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid")BigInteger cid,
                                    @RequestBody CustomerDTO customerDTO) {
        try {
            return ResponseEntity.ok(customerService.create(customerDTO,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create customer: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid")BigInteger cid,@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(customerService.findById(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve customer: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@RequestHeader("cid")BigInteger cid,
                                    @PathVariable("id") BigInteger id,
                                    @RequestBody CustomerDTO customerDTO) {
        try {
            return ResponseEntity.ok(customerService.update(id, customerDTO,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create customer: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(customerService.delete(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete customer: " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(customerService.restore(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to restore customer: " + e.getMessage());
        }
    }
}

package com.example.demo.Controller;

import com.example.demo.DTO.CustomerDTO;
import com.example.demo.Service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid") BigInteger cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(customerService.search(payload, cid));
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid") BigInteger cid) {
        return ResponseEntity.ok(customerService.getAll(cid));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") BigInteger cid,
                                    @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.create(customerDTO, cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(customerService.findById(id, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@RequestHeader("cid") BigInteger cid,
                                    @PathVariable("id") BigInteger id,
                                    @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.update(id, customerDTO, cid));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(customerService.delete(id));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(customerService.restore(id));
    }
}

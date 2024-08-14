package com.example.demo.Controller;

import com.example.demo.DTO.CustomerDTO;
import com.example.demo.Service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(customerService.search(payload, cid));
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid") Long cid) {
        return ResponseEntity.ok(customerService.getAll(cid));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") Long cid,
                                    @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.create(customerDTO, cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.findById(id, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id,
                                    @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.update(id, customerDTO, cid));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.delete(id));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable("id") Long id) {
        return ResponseEntity.ok(customerService.restore(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestHeader("cid")Long cid,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(customerService.importExcel(file,cid));
    }

    @GetMapping("/template")
    public ResponseEntity<?> downloadTemplate() {
        return ResponseEntity.ok(customerService.exportTemplate());

    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(customerService.exportData(payload, cid));
    }
}

package com.example.demo.controller;

import com.example.demo.DTO.ServiceDTO;
import com.example.demo.service.ServiceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/service")
public class ServiceController {
    private final ServiceService serviceService;

    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchService(@RequestHeader("cid") Long cid,
                                           @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(serviceService.search(payload, cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(serviceService.findById(id, cid));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") Long cid,
                                    @RequestBody ServiceDTO serviceDTO) {
        return ResponseEntity.ok(serviceService.create(serviceDTO, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id,
                                    @RequestBody ServiceDTO updatedServiceDTO) {
        return ResponseEntity.ok(serviceService.update(id, updatedServiceDTO, cid));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id) throws Exception {
        return ResponseEntity.ok(serviceService.delete(id, cid));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(serviceService.restore(id, cid));
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid") Long cid) {
        return ResponseEntity.ok(serviceService.getAll(cid));
    }
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestHeader("cid")Long cid,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(serviceService.importExcel(file,cid));
    }

    @GetMapping("/template")
    public ResponseEntity<?> downloadTemplate() {
        return ResponseEntity.ok(serviceService.exportTemplate());

    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(serviceService.exportData(payload, cid));
    }

}

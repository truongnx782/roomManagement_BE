package com.example.demo.controller;

import com.example.demo.DTO.UtilityDTO;
import com.example.demo.service.UtilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.io.IOException;

@RestController
@RequestMapping("/utility")
public class UtilityController {
    private final UtilityService utilityService;

    public UtilityController(UtilityService utilityService) {
        this.utilityService = utilityService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> search(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(utilityService.search(payload, cid));
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid") Long cid) {
        return ResponseEntity.ok(utilityService.getAll(cid));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") Long cid,
                                    @RequestBody UtilityDTO utilityDTO) {
        return ResponseEntity.ok(utilityService.create(utilityDTO, cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(utilityService.findById(id, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> Update(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id,
                                    @RequestBody UtilityDTO utilityDTO) {
        return ResponseEntity.ok(utilityService.update(id, utilityDTO, cid));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id) {
        return ResponseEntity.ok(utilityService.delete(id, cid));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(utilityService.restore(id, cid));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestHeader("cid")Long cid,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(utilityService.importExcel(file,cid));
    }

    @GetMapping("/template")
    public ResponseEntity<?> downloadTemplate() {
        return ResponseEntity.ok(utilityService.exportTemplate());

    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(utilityService.exportData(payload, cid));
    }

}

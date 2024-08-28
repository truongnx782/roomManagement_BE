package com.example.demo.controller;

import com.example.demo.DTO.DtoSecurity.RoomDTO;
import com.example.demo.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchRoom(@RequestHeader("cid") Long cid,
                                        @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(roomService.search(payload, cid));
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid") Long cid) {
        return ResponseEntity.ok(roomService.getAll(cid));
    }

    @GetMapping("/getByPaymentExist")
    public ResponseEntity<?> getByPaymentExist(@RequestHeader("cid") Long cid) {
        return ResponseEntity.ok(roomService.getByPaymentExist(cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.findById(id, cid));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") Long cid,
                                    @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.create(roomDTO, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id,
                                    @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.update(id, roomDTO, cid));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid") Long cid,
                                    @PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.delete(id, cid));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid") Long cid,
                                     @PathVariable("id") Long id) {
        return ResponseEntity.ok(roomService.restore(id, cid));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestHeader("cid")Long cid,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(roomService.importExcel(file,cid));
    }

    @GetMapping("/template")
    public ResponseEntity<?> downloadTemplate() {
        return ResponseEntity.ok(roomService.exportTemplate());

    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestHeader("cid") Long cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(roomService.exportData(payload, cid));
    }
}

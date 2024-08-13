package com.example.demo.Controller;

import com.example.demo.DTO.RoomDTO;
import com.example.demo.Service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {
    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchRoom(@RequestHeader("cid") BigInteger cid,
                                        @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(roomService.search(payload, cid));
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid") BigInteger cid) {
        return ResponseEntity.ok(roomService.getAll(cid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid") BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(roomService.findById(id, cid));
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid") BigInteger cid,
                                    @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.create(roomDTO, cid));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("cid") BigInteger cid,
                                    @PathVariable("id") BigInteger id,
                                    @RequestBody RoomDTO roomDTO) {
        return ResponseEntity.ok(roomService.update(id, roomDTO, cid));
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid") BigInteger cid,
                                    @PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(roomService.delete(id, cid));
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid") BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        return ResponseEntity.ok(roomService.restore(id, cid));
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestHeader("cid")BigInteger cid,
                                        @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(roomService.importExcel(file,cid));
    }

    @GetMapping("/template")
    public ResponseEntity<?> downloadTemplate() {
        return ResponseEntity.ok(roomService.exportTemplate());

    }

    @PostMapping("/export")
    public ResponseEntity<?> export(@RequestHeader("cid") BigInteger cid,
                                    @RequestBody Map<String, Object> payload) {
        return ResponseEntity.ok(roomService.exportData(payload, cid));
    }
}

package com.example.demo.Controller;

import com.example.demo.DTO.RoomDTO;
import com.example.demo.Service.RoomService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> searchRoom(@RequestHeader("cid")BigInteger cid,
                                        @RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(roomService.search(payload,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for Room: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll(@RequestHeader("cid")BigInteger cid) {
        try {
            return ResponseEntity.ok(roomService.getAll(cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for room: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@RequestHeader("cid")BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(roomService.findById(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Room: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestHeader("cid")BigInteger cid,
                                    @RequestBody RoomDTO roomDTO) {
        try {
            return ResponseEntity.ok(roomService.create(roomDTO,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@RequestHeader("cid")BigInteger cid,
                                    @PathVariable("id") BigInteger id,
                                    @RequestBody RoomDTO roomDTO) {
        try {
            return ResponseEntity.ok(roomService.update(id, roomDTO,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update Room: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestHeader("cid")BigInteger cid,
                                    @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(roomService.delete(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete Room: " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@RequestHeader("cid")BigInteger cid,
                                     @PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(roomService.restore(id,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to restore Room: " + e.getMessage());
        }
    }
}

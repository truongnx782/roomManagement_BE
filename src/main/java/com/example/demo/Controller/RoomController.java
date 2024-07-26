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
    public ResponseEntity<?> searchRoom(@RequestBody Map<String, Object> payload) {
        try {
            return ResponseEntity.ok(roomService.search(payload));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for Room: " + e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(roomService.getAll());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to search for room: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(roomService.findById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Room: " + e.getMessage());
        }
    }

    @PostMapping()
    public ResponseEntity<?> create(@RequestBody RoomDTO roomDTO) {
        try {
            return ResponseEntity.ok(roomService.create(roomDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable("id") BigInteger id,
                                    @RequestBody RoomDTO roomDTO) {
        try {
            return ResponseEntity.ok(roomService.update(id, roomDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to update Room: " + e.getMessage());
        }
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(roomService.delete(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to delete Room: " + e.getMessage());
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<?> restore(@PathVariable("id") BigInteger id) {
        try {
            return ResponseEntity.ok(roomService.restore(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to restore Room: " + e.getMessage());
        }
    }
}

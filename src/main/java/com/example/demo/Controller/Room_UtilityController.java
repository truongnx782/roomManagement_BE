package com.example.demo.Controller;

import com.example.demo.DTO.CreateRoom_UtilityDTO;
import com.example.demo.Service.Room_UtilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/room-utility")
public class Room_UtilityController {
    private final Room_UtilityService room_utilityService;

    public Room_UtilityController(Room_UtilityService room_utilityService) {
        this.room_utilityService = room_utilityService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody CreateRoom_UtilityDTO createRoom_utilityDTO) {
        try {
            return ResponseEntity.ok(room_utilityService.create(createRoom_utilityDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody CreateRoom_UtilityDTO createRoom_utilityDTO) {
        try {
            return ResponseEntity.ok(room_utilityService.update(createRoom_utilityDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room: " + e.getMessage());
        }
    }

    @GetMapping("/get-utility-id-by-room-id/{id}")
    public ResponseEntity<?> getUtilityIdByRoomId(@PathVariable("id") BigInteger RoomId) {
        try {
            return ResponseEntity.ok(room_utilityService.getUtilityIdByRoomId(RoomId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room: " + e.getMessage());
        }
    }
}

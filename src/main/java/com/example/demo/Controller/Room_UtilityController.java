package com.example.demo.Controller;

import com.example.demo.Request.Room_UtilityReq;
import com.example.demo.Service.Room_UtilityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;


@RestController
@RequestMapping("/room-utility")
@CrossOrigin(origins = "*",maxAge = 3600)

public class Room_UtilityController {
    private final Room_UtilityService room_utilityService;

    public Room_UtilityController(Room_UtilityService room_utilityService) {
        this.room_utilityService = room_utilityService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Room_UtilityReq createRoom_utilityDTO) {
        try {
            return ResponseEntity.ok(room_utilityService.create(createRoom_utilityDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room-utility: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody Room_UtilityReq createRoom_utilityDTO) {
        try {
            return ResponseEntity.ok(room_utilityService.update(createRoom_utilityDTO));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room-utility: " + e.getMessage());
        }
    }

    @GetMapping("/get-utility-id-by-room-id/{id}")
    public ResponseEntity<?> getUtilityIdByRoomId(@PathVariable("id") BigInteger RoomId) {
        try {
            return ResponseEntity.ok(room_utilityService.getUtilityIdByRoomId(RoomId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Room: " + e.getMessage());
        }
    }
}

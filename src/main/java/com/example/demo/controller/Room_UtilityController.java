package com.example.demo.controller;

import com.example.demo.request.Room_UtilityReq;
import com.example.demo.service.Room_UtilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/room-utility")
public class Room_UtilityController {
    private final Room_UtilityService room_utilityService;

    public Room_UtilityController(Room_UtilityService room_utilityService) {
        this.room_utilityService = room_utilityService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestHeader("cid")Long cid,
                                    @RequestBody Room_UtilityReq createRoom_utilityDTO) {
            return ResponseEntity.ok(room_utilityService.create(createRoom_utilityDTO,cid));
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestHeader("cid")Long cid,
                                    @RequestBody Room_UtilityReq createRoom_utilityDTO) {
            return ResponseEntity.ok(room_utilityService.update(createRoom_utilityDTO,cid));
    }

    @GetMapping("/get-utility-id-by-room-id/{id}")
    public ResponseEntity<?> getUtilityIdByRoomId(@PathVariable("id") Long RoomId) {
            return ResponseEntity.ok(room_utilityService.getUtilityIdByRoomId(RoomId));
    }
}

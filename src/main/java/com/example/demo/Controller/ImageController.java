package com.example.demo.Controller;

import com.example.demo.Service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/image")
public class ImageController {
    private  final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestParam(value = "file",required = false) List<MultipartFile> file,
            @RequestParam(value = "image",required = false) List<BigInteger> images,
            @RequestParam("room") BigInteger roomId,
            @RequestParam("status") Integer status) {
        try {
            return ResponseEntity.ok(imageService.create(file,images, roomId, status));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Image: " + e.getMessage());
        }
    }

    @GetMapping("/get-by-room-id/{id}")
    public ResponseEntity<?> getAllByRoomId(@PathVariable("id") BigInteger RoomId) {
        try {
            return ResponseEntity.ok(imageService.getAllByRoomId(RoomId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Image: " + e.getMessage());
        }
    }
}

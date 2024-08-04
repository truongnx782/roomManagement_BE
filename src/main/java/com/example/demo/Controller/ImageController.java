package com.example.demo.Controller;

import com.example.demo.Service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/image")
@CrossOrigin(origins = "*",maxAge = 3600)

public class ImageController {
    private  final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(
            @RequestHeader("cid")BigInteger cid,
            @RequestParam(value = "file",required = false) List<MultipartFile> file,
            @RequestParam(value = "image",required = false) List<BigInteger> images,
            @RequestParam("room") BigInteger roomId,
            @RequestParam("status") Integer status) {
        try {
            return ResponseEntity.ok(imageService.create(file,images, roomId, status,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to retrieve Image: " + e.getMessage());
        }
    }

    @GetMapping("/get-by-room-id/{id}")
    public ResponseEntity<?> getAllByRoomId(@RequestHeader("cid")BigInteger cid,
                                            @PathVariable("id") BigInteger RoomId) {
        try {
            return ResponseEntity.ok(imageService.getAllByRoomId(RoomId,cid));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to create Image: " + e.getMessage());
        }
    }
}

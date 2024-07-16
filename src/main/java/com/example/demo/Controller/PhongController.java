package com.example.demo.Controller;

import com.example.demo.Repo.PhongRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("phong")
public class PhongController {
    private  final PhongRepo phongRepo;

    public PhongController(PhongRepo phongRepo) {
        this.phongRepo = phongRepo;
    }

    @GetMapping("/hien-thi")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(phongRepo.findAll());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Đã xảy ra lỗi khi lấy dữ liệu từ máy chủ.");
        }
    }
}

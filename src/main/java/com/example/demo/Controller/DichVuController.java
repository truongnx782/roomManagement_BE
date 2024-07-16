package com.example.demo.Controller;

import com.example.demo.Entity.DichVu;
import com.example.demo.Repo.DichVuRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("dich-vu")
public class DichVuController {
    private final DichVuRepo dichVuRepo;

    public DichVuController(DichVuRepo dichVuRepo) {
        this.dichVuRepo = dichVuRepo;
    }

    @GetMapping("/hien-thi")
    public ResponseEntity<Page<DichVu>> getAllWithPagination(
            @RequestParam("page") Optional<Integer> pageParam,
            @RequestParam("size") int pageSizeParam) {
        int page = pageParam.orElse(0);
        Pageable pageable = PageRequest.of(page, pageSizeParam);
        Page<DichVu> data = dichVuRepo.findAll(pageable);
        return ResponseEntity.ok(data);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DichVu> getById(@PathVariable("id") BigInteger id) {
        Optional<DichVu> dichVuOptional = dichVuRepo.findById(id);
        return dichVuOptional.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping()
    public ResponseEntity<DichVu> create(@RequestBody DichVu dichVu) {
        dichVu.setMaDichVu("M");
        dichVu.setTrangThai(1);
        DichVu newDichVu = dichVuRepo.save(dichVu);
        return ResponseEntity.ok(newDichVu);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DichVu> update(@PathVariable("id") BigInteger id, @RequestBody DichVu updatedDichVu) {
        Optional<DichVu> dichVuOptional = dichVuRepo.findById(id);
        if (!dichVuOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        DichVu existingDichVu = dichVuOptional.get();
        existingDichVu.setTenDichVu(updatedDichVu.getTenDichVu());
        existingDichVu.setGiaDichVu(updatedDichVu.getGiaDichVu());
        existingDichVu.setNgayBatDau(updatedDichVu.getNgayBatDau());
        existingDichVu.setNgayKetThuc(updatedDichVu.getNgayKetThuc());
        dichVuRepo.save(existingDichVu);
        return ResponseEntity.ok(existingDichVu);
    }

    @PutMapping("/delete/{id}")
    public ResponseEntity<DichVu> delete(@PathVariable("id") BigInteger id) {
        Optional<DichVu> dichVuOptional = dichVuRepo.findById(id);
        if (!dichVuOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        DichVu existingDichVu = dichVuOptional.get();
        existingDichVu.setTrangThai(0);
        dichVuRepo.save(existingDichVu);
        return ResponseEntity.ok(existingDichVu);
    }
}

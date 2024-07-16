package com.example.demo.Service;

import com.example.demo.DTO.PhongDTO;
import com.example.demo.Entity.Phong;
import com.example.demo.Repo.PhongRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@Service
public class PhongService {
    private final PhongRepo phongRepo;

    public PhongService(PhongRepo phongRepo) {
        this.phongRepo = phongRepo;
    }

    public Page<PhongDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer trangThai = (Integer) payload.get("trangThai");
        Pageable pageable = PageRequest.of(page, size);
        Page<Phong> data = phongRepo.search(search, trangThai, pageable);
        return data.map(Phong::toDTO);
    }

    public Optional<PhongDTO> findById(BigInteger id) throws Exception {
        Optional<Phong> optionalPhong = phongRepo.findById(id);
        if (!optionalPhong.isPresent()) {
            throw new Exception("Phong not fonud");
        }
        return optionalPhong.map(Phong::toDTO);
    }

    public PhongDTO create(PhongDTO phongDTO) {
        Phong phong = Phong.toEntity(phongDTO);
        phong.setMaPhong("M");
        phong.setTrangThai(1);
        Phong newPhong = phongRepo.save(phong);
        return Phong.toDTO(newPhong);
    }

    public PhongDTO update(BigInteger id, PhongDTO phongDTO) throws Exception {
        Optional<Phong> optionalPhong = phongRepo.findById(id);
        if (!optionalPhong.isPresent()) {
           throw  new Exception("Phong not fonud");
        }
        Phong existingPhong = optionalPhong.get();
        existingPhong.setTenPhong(phongDTO.getTenPhong());
        existingPhong.setDiaChi(phongDTO.getDiaChi());
        existingPhong.setDienTich(phongDTO.getDienTich());
        existingPhong.setGiaThue(phongDTO.getGiaThue());
        phongRepo.save(existingPhong);
        return Phong.toDTO(existingPhong);
    }

    public PhongDTO delete(BigInteger id) throws Exception {
        Optional<Phong> optionalPhong = phongRepo.findById(id);
        if (!optionalPhong.isPresent()) {
            throw new Exception("Phong not fonud");
        }
        Phong existingPhong = optionalPhong.get();
        existingPhong.setTrangThai(0);
        phongRepo.save(existingPhong);
        return Phong.toDTO(existingPhong);
    }

}

package com.example.demo.Service;

import com.example.demo.DTO.RoomDTO;
import com.example.demo.Entity.Room;
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

    public Page<RoomDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer trangThai = (Integer) payload.get("trangThai");
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> data = phongRepo.search(search, trangThai, pageable);
        return data.map(Room::toDTO);
    }

    public Optional<RoomDTO> findById(BigInteger id) throws Exception {
        Optional<Room> optionalPhong = phongRepo.findById(id);
        if (!optionalPhong.isPresent()) {
            throw new Exception("Phong not fonud");
        }
        return optionalPhong.map(Room::toDTO);
    }

    public RoomDTO create(RoomDTO phongDTO) {
        Room phong = Room.toEntity(phongDTO);
        phong.setMaPhong("M");
        phong.setTrangThai(1);
        Room newPhong = phongRepo.save(phong);
        return Room.toDTO(newPhong);
    }

    public RoomDTO update(BigInteger id, RoomDTO phongDTO) throws Exception {
        Optional<Room> optionalPhong = phongRepo.findById(id);
        if (!optionalPhong.isPresent()) {
           throw  new Exception("Phong not fonud");
        }
        Room existingPhong = optionalPhong.get();
        existingPhong.setTenPhong(phongDTO.getTenPhong());
        existingPhong.setDiaChi(phongDTO.getDiaChi());
        existingPhong.setDienTich(phongDTO.getDienTich());
        existingPhong.setGiaThue(phongDTO.getGiaThue());
        phongRepo.save(existingPhong);
        return Room.toDTO(existingPhong);
    }

    public RoomDTO delete(BigInteger id) throws Exception {
        Optional<Room> optionalPhong = phongRepo.findById(id);
        if (!optionalPhong.isPresent()) {
            throw new Exception("Phong not fonud");
        }
        Room existingPhong = optionalPhong.get();
        existingPhong.setTrangThai(0);
        phongRepo.save(existingPhong);
        return Room.toDTO(existingPhong);
    }

}

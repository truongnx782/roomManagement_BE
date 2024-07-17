package com.example.demo.Service;

import com.example.demo.DTO.DichVuDTO;
import com.example.demo.Entity.Service;
import com.example.demo.Repo.DichVuRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class DichVuService {
    private final DichVuRepo dichVuRepo;

    public DichVuService(DichVuRepo dichVuRepo) {
        this.dichVuRepo = dichVuRepo;
    }

    public Page<DichVuDTO> searchDichVu(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer trangThai = (Integer) payload.get("trangThai");
        Pageable pageable = PageRequest.of(page, size);
        Page<Service> data = dichVuRepo.search(search, trangThai, pageable);
        return data.map(Service::toDTO);
    }

    public Optional<DichVuDTO> findById(BigInteger id) throws Exception {
        Optional<Service> dichVuOptional = dichVuRepo.findById(id);
        if (!dichVuOptional.isPresent()) {
            throw new Exception("DichVu not fonud");
        }
        return dichVuOptional.map(Service::toDTO);
    }

    public DichVuDTO createDichVu(DichVuDTO dichVuDTO) {
        Service dichVu = Service.toEntity(dichVuDTO);
        dichVu.setMaDichVu("M");
        dichVu.setTrangThai(1);
        Service newDichVu = dichVuRepo.save(dichVu);
        return Service.toDTO(newDichVu);
    }

    public DichVuDTO updateDichVu(BigInteger id, DichVuDTO updatedDichVuDTO) throws Exception {
        Optional<Service> dichVuOptional = dichVuRepo.findById(id);
        if (!dichVuOptional.isPresent()) {
            throw new Exception("DichVu not fonud");
        }
        Service existingDichVu = dichVuOptional.get();
        existingDichVu.setTenDichVu(updatedDichVuDTO.getTenDichVu());
        existingDichVu.setGiaDichVu(updatedDichVuDTO.getGiaDichVu());
        existingDichVu.setNgayBatDau(updatedDichVuDTO.getNgayBatDau());
        existingDichVu.setNgayKetThuc(updatedDichVuDTO.getNgayKetThuc());
        dichVuRepo.save(existingDichVu);
        return Service.toDTO(existingDichVu);
    }

    public DichVuDTO deleteDichVu(BigInteger id) throws Exception {
        Optional<Service> dichVuOptional = dichVuRepo.findById(id);
        if (!dichVuOptional.isPresent()) {
            throw new Exception("DichVu not fonud");
        }
        Service existingDichVu = dichVuOptional.get();
        existingDichVu.setTrangThai(0);
        dichVuRepo.save(existingDichVu);
        return Service.toDTO(existingDichVu);
    }
}


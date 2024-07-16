package com.example.demo.Service;

import com.example.demo.DTO.PhongDTO;
import com.example.demo.DTO.TienIchDTO;
import com.example.demo.Entity.Phong;
import com.example.demo.Entity.TienIch;
import com.example.demo.Repo.TienIchRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TienIchService {
    private  final TienIchRepo tienIchRepo;

    public TienIchService(TienIchRepo tienIchRepo) {
        this.tienIchRepo = tienIchRepo;
    }

    public Page<TienIchDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer trangThai = (Integer) payload.get("trangThai");
        Pageable pageable = PageRequest.of(page, size);
        Page<TienIch> data = tienIchRepo.search(search, trangThai, pageable);
        return data.map(TienIch::toDTO);
    }
}

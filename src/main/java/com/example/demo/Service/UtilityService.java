package com.example.demo.Service;

import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Entity.Utility;
import com.example.demo.repository.UtilityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UtilityService {
    private final UtilityRepository utilityRepository;

    public UtilityService(UtilityRepository utilityRepository) {
        this.utilityRepository = utilityRepository;
    }

    public Page<UtilityDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.get("status");
        Pageable pageable = PageRequest.of(page, size);
        Page<Utility> data = utilityRepository.search(search, status, pageable);
        return data.map(Utility::toDTO);
    }
}

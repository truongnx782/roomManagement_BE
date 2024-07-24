package com.example.demo.Service;

import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Entity.Utility;
import com.example.demo.repository.UtilityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilityService  {
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

    public List<UtilityDTO> getAll() {
        List<Utility> utilityList = utilityRepository.findAllByOrderByIdDesc();
        return utilityList.stream().map(Utility::toDTO).collect(Collectors.toList());
    }

    public UtilityDTO create(UtilityDTO utilityDTO) {
        if (utilityDTO.getUtilityName() == null || utilityDTO.getUtilityName().isEmpty()) {
            throw new IllegalArgumentException(" name cannot be empty.");
        }
        Optional<Utility> maxIdSP = utilityRepository.findMaxId();
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Utility utility = Utility.toEntity(utilityDTO);
        utility.setUtilityCode("U"+maxId);
        utility.setStatus(1);
        Utility newUtility = utilityRepository.save(utility);
        return Utility.toDTO(newUtility);
    }

    public UtilityDTO update(BigInteger id, UtilityDTO utilityDTO) {
        if (utilityDTO.getUtilityName() == null || utilityDTO.getUtilityName().isEmpty()) {
            throw new IllegalArgumentException(" name cannot be empty.");
        }
            Optional<Utility> optionalUtility = utilityRepository.findById(id);
            if (!optionalUtility.isPresent()) {
                throw new IllegalArgumentException("Utility not found");
            }

            Utility utility = optionalUtility.get();
            utility.setUtilityName(utilityDTO.getUtilityName());
            utility=utilityRepository.save(utility);
            return Utility.toDTO(utility);
        }

    public UtilityDTO delete(BigInteger id){
        Optional<Utility> optionalUtility = utilityRepository.findById(id);
        if (!optionalUtility.isPresent()) {
            throw new IllegalArgumentException("Utility not found");
        }
        Utility utility = optionalUtility.get();
        utility.setStatus(0);
        utility=utilityRepository.save(utility);
        return Utility.toDTO(utility);
    }

    public UtilityDTO restore(BigInteger id){
        Optional<Utility> optionalUtility = utilityRepository.findById(id);
        if (!optionalUtility.isPresent()) {
            throw new IllegalArgumentException("Utility not found");
        }
        Utility utility = optionalUtility.get();
        utility.setStatus(1);
        utility=utilityRepository.save(utility);
        return Utility.toDTO(utility);
    }

    public UtilityDTO findById(BigInteger id) {
        Optional<Utility> optionalUtility = utilityRepository.findById(id);
        if (!optionalUtility.isPresent()) {
            throw new IllegalArgumentException("Utility not found");
        }
        return Utility.toDTO(optionalUtility.get());
    }
}

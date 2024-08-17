package com.example.demo.service;

import com.example.demo.DTO.MaintenanceDTO;
import com.example.demo.entity.Maintenance;
import com.example.demo.entity.Room;
import com.example.demo.repository.MaintenanceRepository;
import com.example.demo.util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository) {
        this.maintenanceRepository = maintenanceRepository;
    }

    public Page<MaintenanceDTO> search(Map<String, Object> payload, Long cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status",null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Maintenance> data = maintenanceRepository.search(search, status,cid, pageable);
        return data.map(Maintenance::toDTO);
    }



    public MaintenanceDTO create(MaintenanceDTO maintenanceDTO, Long cid) {
        if (maintenanceDTO.getRoom() == null ) {
            throw new IllegalArgumentException(" room cannot be empty.");
        }
        Maintenance  maintenance = Maintenance.toEntity(maintenanceDTO);
        maintenance.setMaintenanceStatus(Utils.IN_ACTIVE);
        maintenance.setStatus(Utils.ACTIVE);
        maintenance.setCompanyId(cid);
        Maintenance newMaintenance = maintenanceRepository.save(maintenance);
        return Maintenance.toDTO(newMaintenance);
    }

    public MaintenanceDTO update(Long id, MaintenanceDTO maintenanceDTO) {
        if (maintenanceDTO.getRoom() == null ) {
            throw new IllegalArgumentException(" room cannot be empty.");
        }
        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findById(id);
        if (!optionalMaintenance.isPresent()) {
            throw new IllegalArgumentException("Maintenance not found");
        }

        Maintenance maintenance = optionalMaintenance.get();
        maintenance.setRoom(Room.toEntity(maintenanceDTO.getRoom()));
        maintenance.setMaintenanceRequest(maintenanceDTO.getMaintenanceRequest());
        maintenance=maintenanceRepository.save(maintenance);
        return Maintenance.toDTO(maintenance);
    }

    public MaintenanceDTO delete(Long id, Long cid){
        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findByIdAndCompanyId(id,cid);
        if (!optionalMaintenance.isPresent()) {
            throw new IllegalArgumentException("Utility not found");
        }
        Maintenance maintenance = optionalMaintenance.get();
        maintenance.setStatus(Utils.IN_ACTIVE);
        maintenance=maintenanceRepository.save(maintenance);
        return Maintenance.toDTO(maintenance);
    }

    public MaintenanceDTO restore(Long id, Long cid){
        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findByIdAndCompanyId(id,cid);
        if (!optionalMaintenance.isPresent()) {
            throw new IllegalArgumentException("Utility not found");
        }
        Maintenance maintenance = optionalMaintenance.get();
        maintenance.setStatus(Utils.ACTIVE);
        maintenance=maintenanceRepository.save(maintenance);
        return Maintenance.toDTO(maintenance);
    }

    public MaintenanceDTO findById(Long id, Long cid) {
        Optional<Maintenance> optionalMaintenance = maintenanceRepository.findByIdAndCompanyId(id,cid);
        if (!optionalMaintenance.isPresent()) {
            throw new IllegalArgumentException("Maintenance not found");
        }
        return Maintenance.toDTO(optionalMaintenance.get());
    }
}

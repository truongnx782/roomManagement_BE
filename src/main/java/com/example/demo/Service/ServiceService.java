package com.example.demo.Service;

import com.example.demo.DTO.ServiceDTO;
import com.example.demo.Entity.Service;
import com.example.demo.Repo.ServiceRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Page<ServiceDTO> searchService(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.get("status");
        Pageable pageable = PageRequest.of(page, size);
        Page<Service> data = serviceRepository.search(search, status, pageable);
        return data.map(Service::toDTO);
    }

    public Optional<ServiceDTO> findById(BigInteger id) throws Exception {
        Optional<Service> serviceOptional = serviceRepository.findById(id);
        if (!serviceOptional.isPresent()) {
            throw new Exception("Service not found");
        }
        return serviceOptional.map(Service::toDTO);
    }

    public ServiceDTO createService(ServiceDTO serviceDTO) {
        Optional<Service> maxIdSP = serviceRepository.findMaxId();
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Service service = Service.toEntity(serviceDTO);
        service.setServiceCode("S"+maxId);
        service.setStatus(1);
        Service newService = serviceRepository.save(service);
        return Service.toDTO(newService);
    }

    public ServiceDTO updateService(BigInteger id, ServiceDTO updatedServiceDTO) throws Exception {
        Optional<Service> serviceOptional = serviceRepository.findById(id);
        if (!serviceOptional.isPresent()) {
            throw new Exception("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setServiceName(updatedServiceDTO.getServiceName());
        existingService.setServicePrice(updatedServiceDTO.getServicePrice());
        existingService.setStartDate(updatedServiceDTO.getStartDate());
        existingService.setEndDate(updatedServiceDTO.getEndDate());
        serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }

    public ServiceDTO deleteService(BigInteger id) throws Exception {
        Optional<Service> serviceOptional = serviceRepository.findById(id);
        if (!serviceOptional.isPresent()) {
            throw new Exception("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setStatus(0);
        serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }
}

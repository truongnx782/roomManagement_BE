package com.example.demo.Service;

import com.example.demo.DTO.ServiceDTO;
import com.example.demo.Entity.Service;
import com.example.demo.Repo.ServiceRepository;
import com.example.demo.Util.Utils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
public class ServiceService {
    private final ServiceRepository serviceRepository;

    public ServiceService(ServiceRepository serviceRepository) {
        this.serviceRepository = serviceRepository;
    }

    public Page<ServiceDTO> search(Map<String, Object> payload, BigInteger cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status",null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Service> data = serviceRepository.search(search, status,cid, pageable);
        return data.map(Service::toDTO);
    }

    public Optional<ServiceDTO> findById(BigInteger id, BigInteger cid) throws Exception {
        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id,cid);
        if (!serviceOptional.isPresent()) {
            throw new Exception("Service not found");
        }
        return serviceOptional.map(Service::toDTO);
    }

    public ServiceDTO create(ServiceDTO serviceDTO, BigInteger cid) {
        serviceDTO.validateServiceDTO(serviceDTO);
        Optional<Service> maxIdSP = serviceRepository.findMaxId(cid);
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Service service = Service.toEntity(serviceDTO);
        service.setServiceCode("S"+maxId);
        service.setStatus(Utils.ACTIVE);
        service.setCompanyId(cid);
        Service newService = serviceRepository.save(service);
        return Service.toDTO(newService);
    }

    public ServiceDTO update(BigInteger id, ServiceDTO updatedServiceDTO, BigInteger cid) {
        updatedServiceDTO.validateServiceDTO(updatedServiceDTO);

        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id,cid);
        if (!serviceOptional.isPresent()) {
            throw new IllegalArgumentException("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setServiceName(updatedServiceDTO.getServiceName());
        existingService.setServicePrice(updatedServiceDTO.getServicePrice());
        existingService.setStartDate(updatedServiceDTO.getStartDate());
        existingService.setEndDate(updatedServiceDTO.getEndDate());
        existingService=serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }

    public ServiceDTO delete(BigInteger id, BigInteger cid) throws Exception {
        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id,cid);
        if (!serviceOptional.isPresent()) {
            throw new Exception("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setStatus(Utils.IN_ACTIVE);
        existingService=serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }

    public ServiceDTO restore(BigInteger id, BigInteger cid) {
        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id,cid);
        if (!serviceOptional.isPresent()) {
            throw new IllegalArgumentException("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setStatus(Utils.ACTIVE);
        existingService=serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }

    public List<ServiceDTO> getAll(BigInteger cid) {
        List<Service> serviceList = serviceRepository.findAllByCompanyIdOrderByIdDesc(cid);
        return serviceList.stream().map(Service::toDTO).collect(Collectors.toList());
    }
}

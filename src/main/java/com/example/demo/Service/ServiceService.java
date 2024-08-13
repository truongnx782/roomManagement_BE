package com.example.demo.Service;

import com.example.demo.DTO.ServiceDTO;
import com.example.demo.Entity.Service;
import com.example.demo.Repo.ServiceRepository;
import com.example.demo.Util.Excel;
import com.example.demo.Util.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
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
        Integer status = (Integer) payload.getOrDefault("status", null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Service> data = serviceRepository.search(search, status, cid, pageable);
        return data.map(Service::toDTO);
    }

    public Optional<ServiceDTO> findById(BigInteger id, BigInteger cid) throws Exception {
        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id, cid);
        if (!serviceOptional.isPresent()) {
            throw new Exception("Service not found");
        }
        return serviceOptional.map(Service::toDTO);
    }

    public ServiceDTO create(ServiceDTO serviceDTO, BigInteger cid) {
        serviceDTO.validateServiceDTO(serviceDTO);
        Optional<Service> maxIdSP = serviceRepository.findMaxIdByCompanyId(cid);
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Service service = Service.toEntity(serviceDTO);
        service.setServiceCode("S" + maxId);
        service.setStatus(Utils.ACTIVE);
        service.setCompanyId(cid);
        Service newService = serviceRepository.save(service);
        return Service.toDTO(newService);
    }

    public ServiceDTO update(BigInteger id, ServiceDTO updatedServiceDTO, BigInteger cid) {
        updatedServiceDTO.validateServiceDTO(updatedServiceDTO);

        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id, cid);
        if (!serviceOptional.isPresent()) {
            throw new IllegalArgumentException("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setServiceName(updatedServiceDTO.getServiceName());
        existingService.setServicePrice(updatedServiceDTO.getServicePrice());
        existingService.setStartDate(updatedServiceDTO.getStartDate());
        existingService.setEndDate(updatedServiceDTO.getEndDate());
        existingService = serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }

    public ServiceDTO delete(BigInteger id, BigInteger cid) throws Exception {
        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id, cid);
        if (!serviceOptional.isPresent()) {
            throw new Exception("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setStatus(Utils.IN_ACTIVE);
        existingService = serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }

    public ServiceDTO restore(BigInteger id, BigInteger cid) {
        Optional<Service> serviceOptional = serviceRepository.findByIdAndCompanyId(id, cid);
        if (!serviceOptional.isPresent()) {
            throw new IllegalArgumentException("Service not found");
        }
        Service existingService = serviceOptional.get();
        existingService.setStatus(Utils.ACTIVE);
        existingService = serviceRepository.save(existingService);
        return Service.toDTO(existingService);
    }

    public List<ServiceDTO> getAll(BigInteger cid) {
        List<Service> serviceList = serviceRepository.findAllByCompanyIdOrderByIdDesc(cid);
        return serviceList.stream().map(Service::toDTO).collect(Collectors.toList());
    }


    public Object importExcel(MultipartFile file, BigInteger cid) throws IOException {
        Optional<Service> maxIdOpt = serviceRepository.findMaxIdByCompanyId(cid);
        List<Service> existingServices = serviceRepository.findAllByCompanyIdOrderByIdDesc(cid);
        BigInteger maxId = maxIdOpt.map(utility -> utility.getId().add(BigInteger.ONE)).orElse(BigInteger.ONE);

        Set<String> uniqueRows = new HashSet<>();
        List<String> duplicateRows = new ArrayList<>();
        List<Service> services = new ArrayList<>();

        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // Lấy giá trị từ các cột
                String column1 = Excel.getCellValue(row.getCell(0));
                String column2 = Excel.getCellValue(row.getCell(1));
                String column3 = Excel.getCellValue(row.getCell(2));
                String column4 = Excel.getCellValue(row.getCell(3));

                // Tạo chuỗi đại diện cho hàng dữ liệu
                String rowData = column1;

                // Kiểm tra xem hàng dữ liệu đã tồn tại chưa
                boolean isDuplicate = !uniqueRows.add(rowData) || existingServices.stream()
                        .anyMatch(r -> r.getServiceName().equals(column1));

                if (isDuplicate) {
                    duplicateRows.add(rowData);
                    continue; // Chuyển sang hàng tiếp theo
                }

                Service service = new Service();
                service.setServiceCode("S" + maxId);
                service.setServiceName(column1);
                service.setServicePrice(new BigDecimal(column2));

                LocalDate startDate = null;
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    startDate = LocalDate.parse(column3, formatter);
                } catch (DateTimeParseException e) {
                    e.printStackTrace();
                }
                service.setStartDate(startDate);

                if (column4 != null || !column4.isEmpty()) {
                    LocalDate endDate = null;
                    try {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        endDate = LocalDate.parse(column4, formatter);
                    } catch (DateTimeParseException e) {
                        e.printStackTrace();
                    }
                    service.setEndDate(endDate);
                }

                service.setCompanyId(cid);
                service.setStatus(Utils.ACTIVE);
                services.add(service);
                System.out.println(service);
                maxId = maxId.add(BigInteger.ONE);
            }

            List<Service> saveService = serviceRepository.saveAll(services);
            if (!duplicateRows.isEmpty()) {
                throw new IllegalArgumentException("Tên dịch vụ bị trùng: " + duplicateRows);
            }
            return saveService.stream().map(Service::toDTO).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Dữ liệu không đúng định dạng!", e);
        }
    }


    public byte[] exportTemplate() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            // Tạo trang tính với tên "Template"
            Sheet sheet = workbook.createSheet("Template");

            // Tạo hàng tiêu đề
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Service name");
            headerRow.createCell(1).setCellValue("Service price");
            headerRow.createCell(2).setCellValue("Start date");
            headerRow.createCell(3).setCellValue("End date");

            // Tạo CellStyle với định dạng text
            DataFormat format = workbook.createDataFormat();
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(format.getFormat("@")); // "@" là định dạng cho text

            // Áp dụng định dạng text cho tất cả các cột
            for (int i = 0; i < 4; i++) {
                sheet.setDefaultColumnStyle(i, textStyle);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate template", e);
        }
    }

    public byte[] exportData(Map<String, Object> payload, BigInteger cid) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Service name");
            headerRow.createCell(1).setCellValue("Service price");
            headerRow.createCell(2).setCellValue("Start date");
            headerRow.createCell(3).setCellValue("End date");

            // Fetch data
            Page<ServiceDTO> serviceDTOS = search(payload, cid);
            List<Service> serviceList = serviceDTOS.stream()
                    .map(Service::toEntity)
                    .collect(Collectors.toList());

            // Write data to sheet
            int rowIndex = 1;
            for (Service s : serviceList) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(s.getServiceCode());
                row.createCell(1).setCellValue(s.getServiceName());
                row.createCell(2).setCellValue(s.getServicePrice().toString());
                row.createCell(3).setCellValue(s.getStartDate());
                row.createCell(4).setCellValue(s.getEndDate());
                row.createCell(5).setCellValue(s.getStatus());
            }
            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data", e);
        }
    }

}

package com.example.demo.Service;

import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Entity.Utility;
import com.example.demo.Repo.UtilityRepository;
import com.example.demo.Util.Excel;
import com.example.demo.Util.Utils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UtilityService {
    private final UtilityRepository utilityRepository;

    public UtilityService(UtilityRepository utilityRepository) {
        this.utilityRepository = utilityRepository;
    }

    public Page<UtilityDTO> search(Map<String, Object> payload, BigInteger cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status", null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Utility> data = utilityRepository.search(search, status, cid, pageable);
        return data.map(Utility::toDTO);
    }

    public List<UtilityDTO> getAll(BigInteger cid) {
        List<Utility> utilityList = utilityRepository.findAllByCompanyIdOrderByIdDesc(cid);
        return utilityList.stream().map(Utility::toDTO).collect(Collectors.toList());
    }

    public UtilityDTO create(UtilityDTO utilityDTO, BigInteger cid) {
        if (utilityDTO.getUtilityName() == null || utilityDTO.getUtilityName().isEmpty()) {
            throw new IllegalArgumentException(" name cannot be empty.");
        }
        Optional<Utility> optionalUtility =
                utilityRepository.findByUtilityNameAndCompanyId(utilityDTO.getUtilityName(), cid);
        if (optionalUtility.isPresent()) {
            throw new IllegalArgumentException("trùng tên");
        }
        Optional<Utility> maxIdSP = utilityRepository.findMaxIdByCompanyId(cid);
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Utility utility = Utility.toEntity(utilityDTO);
        utility.setUtilityCode("U" + maxId);
        utility.setStatus(1);
        utility.setCompanyId(cid);
        Utility newUtility = utilityRepository.save(utility);
        return Utility.toDTO(newUtility);
    }

    public UtilityDTO update(BigInteger id, UtilityDTO utilityDTO, BigInteger cid) {
        if (utilityDTO.getUtilityName() == null || utilityDTO.getUtilityName().isEmpty()) {
            throw new IllegalArgumentException(" name cannot be empty.");
        }

        Optional<Utility> optional =
                utilityRepository.findByUtilityNameAndCompanyId(utilityDTO.getUtilityName(), cid);
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            throw new IllegalArgumentException("trùng tên");
        }

        Optional<Utility> optionalUtility = utilityRepository.findByIdAndCompanyId(id, cid);
        if (optionalUtility.isEmpty()) {
            throw new IllegalArgumentException("Utility not found");
        }

        Utility utility = optionalUtility.get();
        utility.setUtilityName(utilityDTO.getUtilityName());
        utility = utilityRepository.save(utility);
        return Utility.toDTO(utility);
    }

    public UtilityDTO delete(BigInteger id, BigInteger cid) {
        Optional<Utility> optionalUtility = utilityRepository.findByIdAndCompanyId(id, cid);
        if (optionalUtility.isEmpty()) {
            throw new IllegalArgumentException("Utility not found");
        }
        Utility utility = optionalUtility.get();
        utility.setStatus(0);
        utility = utilityRepository.save(utility);
        return Utility.toDTO(utility);
    }

    public UtilityDTO restore(BigInteger id, BigInteger cid) {
        Optional<Utility> optionalUtility = utilityRepository.findByIdAndCompanyId(id, cid);
        if (optionalUtility.isEmpty()) {
            throw new IllegalArgumentException("Utility not found");
        }
        Utility utility = optionalUtility.get();
        utility.setStatus(Utils.ACTIVE);
        utility = utilityRepository.save(utility);
        return Utility.toDTO(utility);
    }

    public UtilityDTO findById(BigInteger id, BigInteger cid) {
        Optional<Utility> optionalUtility = utilityRepository.findByIdAndCompanyId(id, cid);
        if (optionalUtility.isEmpty()) {
            throw new IllegalArgumentException("Utility not found");
        }
        return Utility.toDTO(optionalUtility.get());
    }


    public Object importExcel(MultipartFile file, BigInteger cid) throws IOException {
        Optional<Utility> maxIdOpt = utilityRepository.findMaxIdByCompanyId(cid);
        List<Utility> existingUtilities = utilityRepository.findAllByCompanyIdOrderByIdDesc(cid);
        BigInteger maxId = maxIdOpt.map(utility -> utility.getId().add(BigInteger.ONE)).orElse(BigInteger.ONE);

        Set<String> uniqueNames = new HashSet<>();
        List<String> duplicateNames = new ArrayList<>();
        List<Utility> utilities = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                String cellValue = Excel.getCellValue(row.getCell(0));
                Utility utility = new Utility();
                utility.setUtilityName(cellValue);
                utility.setUtilityCode("U" + maxId);
                utility.setCompanyId(cid);
                utility.setStatus(Utils.ACTIVE);

                boolean isDuplicate = !uniqueNames.add(cellValue) ||
                        existingUtilities.stream().anyMatch(existing ->
                                existing.getUtilityName().equals(cellValue));

                if (isDuplicate) {
                    duplicateNames.add(cellValue);
                    continue;
                }

                utilities.add(utility);
                maxId = maxId.add(BigInteger.ONE);
            }

            List<Utility> savedUtilities = utilityRepository.saveAll(utilities);
            if (!duplicateNames.isEmpty()) {
                throw new IllegalArgumentException("Tên tiện ích bị trùng: " + duplicateNames);
            }
            return savedUtilities.stream().map(Utility::toDTO).collect(Collectors.toList());
        }
    }


    public byte[] exportTemplate() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Template");
            sheet.createRow(0).createCell(0).setCellValue("Name");

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
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Code");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Status");

            // Fetch data
            Page<UtilityDTO> utilityDTOs = search(payload, cid);
            List<Utility> utilities = utilityDTOs.stream()
                    .map(Utility::toEntity)
                    .collect(Collectors.toList());

            // Write data to sheet
            int rowIndex = 1;
            for (Utility utility : utilities) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(utility.getUtilityCode());
                row.createCell(1).setCellValue(utility.getUtilityName());
                row.createCell(2).setCellValue(utility.getStatus());
            }
            // Tạo CellStyle với định dạng text
            DataFormat format = workbook.createDataFormat();
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(format.getFormat("@")); // "@" là định dạng cho text

            // Áp dụng định dạng text cho tất cả các cột
            for (int i = 0; i < 3; i++) {
                sheet.setDefaultColumnStyle(i, textStyle);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data", e);
        }
    }

}

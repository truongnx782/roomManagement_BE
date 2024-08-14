package com.example.demo.Service;

import com.example.demo.DTO.ContractDTO;
import com.example.demo.DTO.RoomDTO;
import com.example.demo.DTO.UtilityDTO;
import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Room;
import com.example.demo.Entity.Utility;
import com.example.demo.Repo.RoomRepository;
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
import java.math.BigDecimal;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<RoomDTO> search(Map<String, Object> payload, Long cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status",null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> data = roomRepository.search(search, status,cid, pageable);
        return data.map(Room::toDTO);
    }

    public List<RoomDTO> getAll(Long cid) {
        List<Room> rooms = roomRepository.findAllByCompanyIdOrderByIdDesc(cid);
        return rooms.stream().map(Room::toDTO).collect(Collectors.toList());
    }


    public Optional<RoomDTO> findById(Long id, Long cid)  {
        Optional<Room> roomOptional = roomRepository.findByIdAndCompanyId(id,cid);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        return roomOptional.map(Room::toDTO);
    }

    public RoomDTO create(RoomDTO roomDTO, Long cid) {
        roomDTO.validateRoomDTO(roomDTO);
        Optional<Room> roomOptional =
                roomRepository.findByRoomNameAndCompanyId(roomDTO.getRoomName(), cid);
        if (roomOptional.isPresent()) {
            throw new IllegalArgumentException("trùng tên");
        }
        Optional<Room> maxIdSP = roomRepository.findMaxIdByCompanyId(cid);
        Long maxId = maxIdSP.isPresent() ? maxIdSP.get().getId()+1 : 1;

        Room room = Room.toEntity(roomDTO);
        room.setRoomCode("R"+maxId);
        room.setStatus(Utils.ACTIVE);
        room.setCompanyId(cid);
        Room newRoom = roomRepository.save(room);
        return Room.toDTO(newRoom);
    }

    public RoomDTO update(Long id, RoomDTO roomDTO, Long cid)  {
        roomDTO.validateRoomDTO(roomDTO);

        Optional<Room> optional =
                roomRepository.findByRoomNameAndCompanyId(roomDTO.getRoomName(), cid);
        if (optional.isPresent() && !optional.get().getId().equals(id)) {
            throw new IllegalArgumentException("trùng tên");
        }

        Optional<Room> roomOptional = roomRepository.findByIdAndCompanyId(id,cid);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        Room existingRoom = roomOptional.get();
        existingRoom.setRoomName(roomDTO.getRoomName());
        existingRoom.setAddress(roomDTO.getAddress());
        existingRoom.setArea(roomDTO.getArea());
        existingRoom.setRentPrice(roomDTO.getRentPrice());
        existingRoom=roomRepository.save(existingRoom);
        return Room.toDTO(existingRoom);
    }

    public RoomDTO delete(Long id, Long cid)  {
        Optional<Room> roomOptional = roomRepository.findByIdAndCompanyId(id,cid);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        Room existingRoom = roomOptional.get();
        existingRoom.setStatus(Utils.IN_ACTIVE);
        existingRoom=roomRepository.save(existingRoom);
        return Room.toDTO(existingRoom);
    }

    public RoomDTO restore(Long id, Long cid) {
        Optional<Room> roomOptional = roomRepository.findByIdAndCompanyId(id,cid);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        Room existingRoom = roomOptional.get();
        existingRoom.setStatus(Utils.ACTIVE);
        existingRoom=roomRepository.save(existingRoom);
        return Room.toDTO(existingRoom);
    }


    public Object importExcel(MultipartFile file, Long cid) throws IOException {
        Optional<Room> maxIdSP = roomRepository.findMaxIdByCompanyId(cid);
        List<Room> existingRooms = roomRepository.findAllByCompanyIdOrderByIdDesc(cid);
        Long maxId = maxIdSP.isPresent() ? maxIdSP.get().getId()+1 : 1;

        List<Room> roomList = new ArrayList<>();
        Set<String> uniqueRows = new HashSet<>();
        List<String> duplicateRows = new ArrayList<>();

        try (InputStream is = file.getInputStream(); Workbook workbook = new XSSFWorkbook(is)) {
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Bỏ qua hàng tiêu đề
                Row row = sheet.getRow(i);
                if (row == null) continue; // Bỏ qua các hàng trống

                // Lấy giá trị từ các cột
                String column1 = Excel.getCellValue(row.getCell(0));
                String column2 = Excel.getCellValue(row.getCell(1));
                String column3 = Excel.getCellValue(row.getCell(2));
                String column4 = Excel.getCellValue(row.getCell(3));

                // Tạo chuỗi đại diện cho hàng dữ liệu
                String rowData = column1 + "_"+ column3 ;

                // Kiểm tra xem hàng dữ liệu đã tồn tại chưa
                boolean isDuplicate = !uniqueRows.add(rowData) || existingRooms.stream()
                        .anyMatch(r -> r.getRoomName().equals(column1) &&
                                r.getAddress().equals(column4));

                if (isDuplicate) {
                    duplicateRows.add(rowData);
                    continue; // Chuyển sang hàng tiếp theo
                }

                Room room = new Room();
                room.setRoomName(column1);
                room.setArea(column2);
                room.setRentPrice(new BigDecimal(column3));
                room.setAddress(column4);
                room.setRoomCode("R" + maxId);
                room.setCompanyId(cid);
                room.setStatus(Utils.ACTIVE);
                roomList.add(room);
                maxId = maxId+1;
            }

            List<Room> result=roomRepository.saveAll(roomList);

            if (!duplicateRows.isEmpty()) {
                throw new IllegalArgumentException("Dữ liệu bị trùng: " + duplicateRows);
            }

            return result.stream()
                    .map(Room::toDTO)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException("Dữ liệu không đúng định dạng!", e);
        }
    }


    public byte[] exportTemplate() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Template");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Area");
            headerRow.createCell(2).setCellValue("Rent price");
            headerRow.createCell(3).setCellValue("Address");

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to generate template", e);
        }
    }


    public byte[] exportData(Map<String, Object> payload, Long cid) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Data");
            // Create header row
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Code");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Area");
            header.createCell(3).setCellValue("Rent price");
            header.createCell(4).setCellValue("Address");
            header.createCell(5).setCellValue("Rent status");
            header.createCell(6).setCellValue("Status");

            // Fetch data
            Page<RoomDTO> roomDTOS = search(payload, cid);
            List<Room> rooms = roomDTOS.stream()
                    .map(Room::toEntity)
                    .collect(Collectors.toList());

            // Write data to sheet
            int rowIndex = 1;
            for (Room r : rooms) {
                Row row = sheet.createRow(rowIndex++);
                row.createCell(0).setCellValue(r.getRoomCode());
                row.createCell(1).setCellValue(r.getRoomName());
                row.createCell(2).setCellValue(r.getArea());
                row.createCell(3).setCellValue( r.getRentPrice().toString());
                row.createCell(4).setCellValue(r.getAddress());
                row.createCell(5).setCellValue(r.getRentStatus());
                row.createCell(6).setCellValue(r.getStatus());
            }

            // Tạo CellStyle với định dạng text
            DataFormat format = workbook.createDataFormat();
            CellStyle textStyle = workbook.createCellStyle();
            textStyle.setDataFormat(format.getFormat("@")); // "@" là định dạng cho text

            // Áp dụng định dạng text cho tất cả các cột
            for (int i = 0; i < 7; i++) {
                sheet.setDefaultColumnStyle(i, textStyle);
            }

            workbook.write(baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to export data", e);
        }
    }
}

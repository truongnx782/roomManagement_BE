package com.example.demo.Service;

import com.example.demo.DTO.RoomDTO;
import com.example.demo.Entity.Room;
import com.example.demo.Repo.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Map;
import java.util.Optional;

@Service
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<RoomDTO> search(Map<String, Object> payload) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.get("status");
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> data = roomRepository.search(search, status, pageable);
        return data.map(Room::toDTO);
    }

    public Optional<RoomDTO> findById(BigInteger id)  {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        return roomOptional.map(Room::toDTO);
    }

    public RoomDTO create(RoomDTO roomDTO) {
        roomDTO.validateRoomDTO(roomDTO);

        Optional<Room> maxIdSP = roomRepository.findMaxId();
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Room room = Room.toEntity(roomDTO);
        room.setRoomCode("R"+maxId);
        room.setStatus(1);
        Room newRoom = roomRepository.save(room);
        return Room.toDTO(newRoom);
    }

    public RoomDTO update(BigInteger id, RoomDTO roomDTO)  {
        roomDTO.validateRoomDTO(roomDTO);

        Optional<Room> roomOptional = roomRepository.findById(id);
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

    public RoomDTO delete(BigInteger id) throws Exception {
        Optional<Room> roomOptional = roomRepository.findById(id);
        if (!roomOptional.isPresent()) {
            throw new Exception("Room not found");
        }
        Room existingRoom = roomOptional.get();
        existingRoom.setStatus(0); // Example logical delete operation
        existingRoom=roomRepository.save(existingRoom);
        return Room.toDTO(existingRoom);
    }
}

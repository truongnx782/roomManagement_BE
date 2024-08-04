package com.example.demo.Service;

import com.example.demo.DTO.ContractDTO;
import com.example.demo.DTO.RoomDTO;
import com.example.demo.Entity.Contract;
import com.example.demo.Entity.Room;
import com.example.demo.Repo.RoomRepository;
import com.example.demo.Util.Utils;
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
public class RoomService {
    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Page<RoomDTO> search(Map<String, Object> payload, BigInteger cid) {
        int page = (int) payload.getOrDefault("page", 0);
        int size = (int) payload.getOrDefault("size", 5);
        String search = (String) payload.getOrDefault("search", "");
        Integer status = (Integer) payload.getOrDefault("status",null);
        Pageable pageable = PageRequest.of(page, size);
        Page<Room> data = roomRepository.search(search, status,cid, pageable);
        return data.map(Room::toDTO);
    }

    public List<RoomDTO> getAll(BigInteger cid) {
        List<Room> rooms = roomRepository.findAllByCompanyIdOrderByIdDesc(cid);
        return rooms.stream().map(Room::toDTO).collect(Collectors.toList());
    }


    public Optional<RoomDTO> findById(BigInteger id, BigInteger cid)  {
        Optional<Room> roomOptional = roomRepository.findByIdAndCompanyId(id,cid);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        return roomOptional.map(Room::toDTO);
    }

    public RoomDTO create(RoomDTO roomDTO, BigInteger cid) {
        roomDTO.validateRoomDTO(roomDTO);

        Optional<Room> maxIdSP = roomRepository.findMaxIdByCompanyId(cid);
        BigInteger maxId = maxIdSP.isPresent() ? maxIdSP.get().getId().add(BigInteger.ONE) : BigInteger.ONE;

        Room room = Room.toEntity(roomDTO);
        room.setRoomCode("R"+maxId);
        room.setStatus(Utils.ACTIVE);
        room.setCompanyId(cid);
        Room newRoom = roomRepository.save(room);
        return Room.toDTO(newRoom);
    }

    public RoomDTO update(BigInteger id, RoomDTO roomDTO, BigInteger cid)  {
        roomDTO.validateRoomDTO(roomDTO);

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

    public RoomDTO delete(BigInteger id, BigInteger cid)  {
        Optional<Room> roomOptional = roomRepository.findByIdAndCompanyId(id,cid);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        Room existingRoom = roomOptional.get();
        existingRoom.setStatus(Utils.IN_ACTIVE);
        existingRoom=roomRepository.save(existingRoom);
        return Room.toDTO(existingRoom);
    }

    public RoomDTO restore(BigInteger id, BigInteger cid) {
        Optional<Room> roomOptional = roomRepository.findByIdAndCompanyId(id,cid);
        if (!roomOptional.isPresent()) {
            throw new IllegalArgumentException("Room not found");
        }
        Room existingRoom = roomOptional.get();
        existingRoom.setStatus(Utils.ACTIVE);
        existingRoom=roomRepository.save(existingRoom);
        return Room.toDTO(existingRoom);
    }
}

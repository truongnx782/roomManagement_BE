package com.example.demo.service;

import com.example.demo.DTO.Room_UtilityDTO;
import com.example.demo.entity.Room;
import com.example.demo.entity.Room_Utility;
import com.example.demo.entity.Utility;
import com.example.demo.repository.Room_UtilityRepository;
import com.example.demo.request.Room_UtilityReq;
import com.example.demo.util.Utils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Room_UtilityService {
    private final Room_UtilityRepository room_utilityRepository;

    public Room_UtilityService(Room_UtilityRepository room_utilityRepository) {
        this.room_utilityRepository = room_utilityRepository;
    }

    public List<Room_UtilityDTO> create(Room_UtilityReq createRoom_utilityDTO, Long cid) {
        List<Room_Utility> room_utilitys = new ArrayList<>();

        for (int i = 0; i < createRoom_utilityDTO.getUtilitys().size(); i++) {
            Room_Utility newRoomUtility = new Room_Utility();

            Room room = new Room();
            room.setId(createRoom_utilityDTO.getRoom());
            Utility utility = new Utility();
            utility.setId(createRoom_utilityDTO.getUtilitys().get(i));

            newRoomUtility.setRoom(room);
            newRoomUtility.setUtility(utility);
            newRoomUtility.setStatus(Utils.ACTIVE);
            newRoomUtility.setCompanyId(cid);
            room_utilitys.add(newRoomUtility);
        }
        room_utilitys = room_utilityRepository.saveAll(room_utilitys);

        List<Room_UtilityDTO> result = room_utilitys.stream()
                .map(Room_Utility::toDTO)
                .collect(Collectors.toList());
        return result;
    }

    public List<Long> getUtilityIdByRoomId(Long roomId) {
        List<Long> utilityIdList = room_utilityRepository.findAllUtilityIdByRoomId(roomId);
        return utilityIdList;

    }

    public List<Room_UtilityDTO>  update(Room_UtilityReq createRoom_utilityDTO, Long cid) {
        room_utilityRepository.deleteByRoomIdAndCompanyId(createRoom_utilityDTO.getRoom(),cid);

        List<Room_Utility> room_utilitys = new ArrayList<>();
        for (int i = 0; i < createRoom_utilityDTO.getUtilitys().size(); i++) {
            Room_Utility newRoomUtility = new Room_Utility();

            Room room = new Room();
            room.setId(createRoom_utilityDTO.getRoom());
            Utility utility = new Utility();
            utility.setId(createRoom_utilityDTO.getUtilitys().get(i));

            newRoomUtility.setRoom(room);
            newRoomUtility.setUtility(utility);
            newRoomUtility.setStatus(Utils.ACTIVE);
            newRoomUtility.setCompanyId(cid);
            room_utilitys.add(newRoomUtility);
        }
        room_utilitys = room_utilityRepository.saveAll(room_utilitys);

        List<Room_UtilityDTO> result = room_utilitys.stream()
                .map(Room_Utility::toDTO)
                .collect(Collectors.toList());
        return result;
    }
}

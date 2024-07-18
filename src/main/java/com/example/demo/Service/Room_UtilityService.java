package com.example.demo.Service;

import com.example.demo.DTO.CreateRoom_UtilityDTO;
import com.example.demo.DTO.Room_UtilityDTO;
import com.example.demo.Entity.Room;
import com.example.demo.Entity.Room_Utility;
import com.example.demo.Entity.Utility;
import com.example.demo.Repo.Room_UtilityRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Room_UtilityService {
    private final Room_UtilityRepository room_utilityRepository;

    public Room_UtilityService(Room_UtilityRepository room_utilityRepository) {
        this.room_utilityRepository = room_utilityRepository;
    }

    public List<Room_UtilityDTO> create(CreateRoom_UtilityDTO createRoom_utilityDTO) {
        List<Room_Utility> room_utilitys = new ArrayList<>();

        for (int i = 0; i < createRoom_utilityDTO.getUtilitys().size(); i++) {
            Room_Utility newRoomUtility = new Room_Utility();

            Room room = new Room();
            room.setId(createRoom_utilityDTO.getRoom());
            Utility utility = new Utility();
            utility.setId(createRoom_utilityDTO.getUtilitys().get(i));

            newRoomUtility.setRoom(room);
            newRoomUtility.setUtility(utility);
            newRoomUtility.setStatus(1);

            room_utilitys.add(newRoomUtility);
        }
        room_utilitys = room_utilityRepository.saveAll(room_utilitys);

        List<Room_UtilityDTO> result = room_utilitys.stream()
                .map(Room_Utility::toDTO)
                .collect(Collectors.toList());
        return result;
    }

    public List<BigInteger> getUtilityIdByRoomId(BigInteger roomId) {
        List<BigInteger> utilityIdList = room_utilityRepository.findAllUtilityIdByRoomId(roomId);
        return utilityIdList;

    }

    public List<Room_UtilityDTO>  update(CreateRoom_UtilityDTO createRoom_utilityDTO) {
        room_utilityRepository.deleteByRoomId(createRoom_utilityDTO.getRoom());

        List<Room_Utility> room_utilitys = new ArrayList<>();
        for (int i = 0; i < createRoom_utilityDTO.getUtilitys().size(); i++) {
            Room_Utility newRoomUtility = new Room_Utility();

            Room room = new Room();
            room.setId(createRoom_utilityDTO.getRoom());
            Utility utility = new Utility();
            utility.setId(createRoom_utilityDTO.getUtilitys().get(i));

            newRoomUtility.setRoom(room);
            newRoomUtility.setUtility(utility);
            newRoomUtility.setStatus(1);

            room_utilitys.add(newRoomUtility);
        }
        room_utilitys = room_utilityRepository.saveAll(room_utilitys);

        List<Room_UtilityDTO> result = room_utilitys.stream()
                .map(Room_Utility::toDTO)
                .collect(Collectors.toList());
        return result;
    }
}

package com.example.demo.Entity;

import com.example.demo.DTO.ImageDTO;
import com.example.demo.DTO.RoomDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "Image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "RoomId")
    private Room room;

    @Column(name = "Status")
    private int status;

    @Column(name = "CompanyId")
    private BigInteger companyId;

    @Column(name = "Url")
    private String url;

    public static ImageDTO toDTO(Image image) {
        return ImageDTO.builder()
                .id(image.getId())
                .room(Room.toDTO(image.getRoom()))
                .url(image.getUrl())
                .status(image.getStatus())
                .companyId(image.getCompanyId())
                .build();
    }

    public static Image toEntity(ImageDTO ImageDTO) {
        return Image.builder()
                .id(ImageDTO.getId())
                .room(Room.toEntity(ImageDTO.getRoom()))
                .url(ImageDTO.getUrl())
                .status(ImageDTO.getStatus())
                .companyId(ImageDTO.getCompanyId())
                .build();
    }
}

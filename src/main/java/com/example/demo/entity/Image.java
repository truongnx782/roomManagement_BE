package com.example.demo.entity;

import com.example.demo.DTO.ImageDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "RoomId", nullable = false)
    private Room room;

    @Column(name = "Status", nullable = false)
    private Integer status;

    @Column(name = "CompanyId", nullable = false)
    private Long companyId;

    @Column(name = "Url", nullable = false)
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

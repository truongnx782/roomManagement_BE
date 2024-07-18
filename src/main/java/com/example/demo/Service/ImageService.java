package com.example.demo.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.DTO.ImageDTO;
import com.example.demo.Entity.Image;
import com.example.demo.Entity.Room;
import com.example.demo.Repo.ImageRepositoty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final ImageRepositoty imageRepositoty;
    private final Cloudinary cloudinary;
    private final ExecutorService executorService;

    public ImageService(ImageRepositoty imageRepositoty, Cloudinary cloudinary) {
        this.imageRepositoty = imageRepositoty;
        this.cloudinary = cloudinary;
        this.executorService = Executors.newFixedThreadPool(5);
    }

    // Nén ảnh, tăng tốc độ upload
    public List<ImageDTO> create(List<MultipartFile> files, List<BigInteger> images, BigInteger roomId, Integer status) throws IOException {
        List<Image> listImage = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                Map r = this.cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap("resource_type", "auto"));
                Image image = new Image();
                Room room = new Room();
                room.setId(roomId);
                image.setRoom(room);
                image.setUrl((String) r.get("secure_url"));
                image.setStatus(status);
                listImage.add(image);
            }
        }

        List<Image> imagesRepo = imageRepositoty.findAllByRoomId(roomId);
        List<BigInteger> imageRemove;

        if (images == null) {
            imageRemove = imagesRepo.stream()
                    .map(Image::getId)
                    .collect(Collectors.toList());
        } else {
            imageRemove = imagesRepo.stream()
                    .map(Image::getId)
                    .filter(imageId -> !images.contains(imageId))
                    .collect(Collectors.toList());
        }

        // Xóa ảnh khỏi Cloudinary
        imagesRepo.stream()
                .filter(image -> imageRemove.contains(image.getId()))
                .forEach(image -> {
                    try {
                        deleteImageByPublicId(image.getUrl());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        // Xóa ảnh khỏi repository
        imageRepositoty.deleteAllById(imageRemove);

        listImage = imageRepositoty.saveAll(listImage);
        return listImage.stream()
                .map(Image::toDTO)
                .collect(Collectors.toList());
    }

    public void deleteImageByPublicId(String url) throws IOException {
        String publicId = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf('.'));
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

    public List<ImageDTO> getAllByRoomId(BigInteger roomId) {
        List<Image> images = imageRepositoty.findAllByRoomId(roomId);
        return images.stream()
                .map(Image::toDTO)
                .collect(Collectors.toList());
    }








//    // nén ảnh, tăng tốc độ upload
//    public List<Image> create(List<MultipartFile> files, BigInteger roomId, Integer status) {
//        List<Image> listImage = new ArrayList<>();
//        files.forEach(file -> executorService.execute(() -> {
//            try {
//                byte[] compressedImage = compressImage(file.getBytes());
//                Map uploadResult = cloudinary.uploader().upload(compressedImage, ObjectUtils.asMap("resource_type", "auto"));
//                Image image = new Image();
//                Room room = new Room();
//                room.setId(roomId);
//                image.setRoom(room);
//                image.setUrl((String) uploadResult.get("secure_url"));
//                image.setStatus(status);
//                synchronized (listImage) {
//                    listImage.add(image);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }));
//        executorService.shutdown();
//        return imageRepositoty.saveAll(listImage);
//    }
//
//    private byte[] compressImage(byte[] imageData) {
//        // Triển khai logic nén ảnh ở đây, sử dụng ImageIO và BufferedImage
//        return imageData; // Đang trả về dữ liệu ảnh chưa nén, cần triển khai nén thực tế
//    }
}

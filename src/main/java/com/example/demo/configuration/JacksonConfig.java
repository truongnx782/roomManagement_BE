//package com.example.demo.configuration;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.text.SimpleDateFormat;
//
//@Configuration
//public class JacksonConfig {
//
//    // Định nghĩa bean ObjectMapper để cấu hình cách thức Jackson xử lý JSON
//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper mapper = new ObjectMapper(); // Tạo một đối tượng ObjectMapper mới
//
//        // Định dạng ngày tháng theo kiểu "yyyy-MM-dd HH:mm:ss.SSSSSS"
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSSSS");
//        mapper.setDateFormat(dateFormat); // Cấu hình ObjectMapper để sử dụng định dạng ngày tháng đã chỉ định
//
//        // Tắt chế độ ghi ngày tháng dưới dạng timestamp
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//
//        return mapper; // Trả về đối tượng ObjectMapper đã cấu hình
//    }
//}

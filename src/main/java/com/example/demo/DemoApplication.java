package com.example.demo;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.demo.Service.PaymentService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@EnableScheduling
public class DemoApplication {
//    @Autowired
//    private PaymentService paymentService;

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

//        @PostConstruct
//        public void init() {
//            paymentService.autoCreatePayment();
//        }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "dzdfzxbmy",
                "api_key", "989353715489512",
                "api_secret", "eESrJoEBv4SfjogF1UJmuDeAIm8",
                "secure", true
        ));
    }
}

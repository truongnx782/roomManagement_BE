//package com.example.demo.security.jwt;
//
//import com.example.demo.security.service.UserDetailsImpl;
//import io.jsonwebtoken.*;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.ResponseCookie;
//import org.springframework.stereotype.Component;
//import org.springframework.web.util.WebUtils;
//
//
//import java.util.Date;
//
//@Component
//public class JwtUtils {
//
//    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
//
//    @Value("${jwtSecret}")
//    private String jwtSecret;
//
//    @Value("${jwtExpirationMs}")
//    private int jwtExpirationMs;
//
//    @Value("${jwtCookieName}")
//    private String jwtCookie;
//
//    // Sửa phương thức này trong AuthTokenFilter để lấy token từ header
//    public String getJwtFromHeader(HttpServletRequest request) {
//        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            return authHeader.substring(7); // Loại bỏ "Bearer " và trả về token
//        }
//        return null;
//    }
//
//
//    public ResponseCookie generateJwtCookie(UserDetailsImpl userPrincipal) {
//        String jwt = generateTokenFromUsername(userPrincipal.getUsername());
//        return ResponseCookie.from("token", jwt) // Đặt tên cookie là "token"
//                .path("/") // Đặt path là "/"
//               .maxAge(24 * 60 * 60) // Thời gian sống 24 giờ
////                .maxAge(5 * 60) // Thời gian sống 5 phút
//                .httpOnly(false) // Cho phép JavaScript đọc cookie (nếu cần)
//                .secure(false) // Đặt thành true nếu sử dụng HTTPS
//                .build();
//    }
//
//
//    public ResponseCookie getCleanJwtCookie() {
//        return ResponseCookie.from("jwt", "")
//                .path("/")
//                .httpOnly(false)
//                .secure(false)
//                .maxAge(0)
//                .build();
//    }
//
//
//    public String getUserNameFromJwtToken(String token) {
//        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
//    }
//
//    public boolean validateJwtToken(String authToken) {
//        try {
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//            return true;
//        } catch (JwtException e) {
//            logger.error("JWT token validation error: {}", e.getMessage());
//            return false;
//        }
//    }
//
//
//    private String generateTokenFromUsername(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date(new Date().getTime() + jwtExpirationMs))
//                .signWith(SignatureAlgorithm.HS512, jwtSecret)
//                .compact();
//    }
//}

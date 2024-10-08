package com.example.demo.controller;

import com.example.demo.DTO.DtoSecurity.*;
import com.example.demo.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ResponseEntity.ok()
                .header("token", result.getToken())
                .header("cid", result.getCid().toString())
                .body("ok");
    }

    @PostMapping("/register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegisterRequest request) throws MessagingException {
        authenticationService.register(request);
        return ResponseEntity.ok(Map.of("message","SENDING_MAIL_SUCCESSFULLY"));

    }

    @GetMapping("/confirm-register")
    public ResponseEntity<?> confirmRegister(@RequestParam("email")String email,
                                             @RequestParam("password")String password,
                                             @RequestParam("otp") String otp) {
        authenticationService.confirmRegister(email,password,otp);
        return ResponseEntity.ok("Xác thực tài khoản thành công!");
    }

    // Endpoint để kiểm tra tính hợp lệ của token
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }


    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody Map<String, Object> objectMap) throws ParseException, JOSEException {
        String request = (String) objectMap.get("token");
        authenticationService.logout(request);
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/login-google")
    public ResponseEntity<?> handleGoogleLogin(@RequestBody Map<String, Object> payload) {
        var authenticationRequest = authenticationService.handleGoogleLogin(payload);

        var result = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok()
                .header("token", result.getToken())
                .header("cid", result.getCid().toString())
                .body("ok");

    }

//    @GetMapping("/login-success")
//    public ResponseEntity<?> loginSuccess(@AuthenticationPrincipal OAuth2User principal) {
//        // Trả về thông tin người dùng đăng nhập thành công
//
//        Map<String,Object> objectMap = new HashMap<>();
//        objectMap.put("email",principal.getAttribute("email"));
//        objectMap.put("name",principal.getAttribute("name"));
//        objectMap.put("sub",principal.getAttribute("sub"));
//        objectMap.put("picture",principal.getAttribute("picture"));
//
//
//        var authenticationRequest = authenticationService.handleGoogleLogin(objectMap);
//
//        var result = authenticationService.authenticate(authenticationRequest);
//        System.out.println(result.getToken());
//        return ResponseEntity.ok()
//                .header("token", result.getToken())
//                .header("cid", result.getCid().toString())
//                .body("ok");
//    }

//    @GetMapping("/login-failure")
//    public String loginFailure() {
//        // Xử lý khi đăng nhập thất bại
//        return "Login failed";
//    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, Object> payload) throws MessagingException {
        authenticationService.forgotPassword(payload);
        return ResponseEntity.ok(Map.of("message","SENDING_MAIL_SUCCESSFULLY"));
    }

    @GetMapping("/confirm-forgot-password")
    public ResponseEntity<?> confirmForgotPassword(@RequestParam("email")String email,
                                                   @RequestParam("password")String password,
                                                   @RequestParam("otp") String otp) {
        authenticationService.confirmForgotPassword(email,password,otp);
        return ResponseEntity.ok("Xác thực tài khoản thành công!");
    }

    @PostMapping("/check-token")
    public ResponseEntity<Map<String, Object>> validateToken
            (@RequestHeader("Authorization") String token) throws ParseException, JOSEException {

       var result= authenticationService.validateToken(token);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refresh(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body("Invalid token");
        }

        try {
            var result = authenticationService.refreshToken(token);

            return ResponseEntity.ok()
                    .header("token", result.getToken())
                    .header("cid", result.getCid().toString())
                    .body("Token refreshed successfully");
        } catch (ParseException | JOSEException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Failed to refresh token");
        }
    }


}

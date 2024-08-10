package com.example.demo.Controller;

import com.example.demo.DTO.DtoSecurity.*;
import com.example.demo.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;


    // Endpoint để đăng nhập và lấy JWT
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ResponseEntity.ok()
                .header("token", result.getToken())
                .header("cid", result.getCid().toString())
                .body("ok");
    }

    @PostMapping("/register")
    public ResponseEntity<?> Register(@Valid @RequestBody RegisterRequest request) {
        authenticationService.register(request);
        return ResponseEntity.ok("submitted successfully");
    }

    @GetMapping("/confirm-register")
    public ResponseEntity<?> confirmRegister(@RequestParam("email")String email,
                                             @RequestParam("otp") String otp) {
        authenticationService.confirmRegister(email,otp);
        return ResponseEntity.ok("Xác thực thành công");
    }

    // Endpoint để kiểm tra tính hợp lệ của token
    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        // Gọi service để kiểm tra tính hợp lệ của token
        var result = authenticationService.introspect(request);

        // Trả về phản hồi với kết quả kiểm tra token
        return ApiResponse.<IntrospectResponse>builder().result(result).build();
    }

    // Endpoint để làm mới token
    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        // Gọi service để làm mới token
        var result = authenticationService.refreshToken(request);

        // Trả về phản hồi với kết quả làm mới token
        return ResponseEntity.ok()
                .header("token", result.getToken())
                .header("cid", result.getCid().toString())
                .body("ok");
    }

    // Endpoint để đăng xuất và thu hồi token
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody Map<String, Object> objectMap) throws ParseException, JOSEException {
        String request = (String) objectMap.get("token");
        // Gọi service để thực hiện đăng xuất
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

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, Object> payload){
        authenticationService.forgotPassword(payload);
        return ResponseEntity.ok("submitted successfully");
    }
    @GetMapping("/confirm-forgot-password")
    public ResponseEntity<?> confirmForgotPassword(@RequestParam("email")String email,
                                                   @RequestParam("password")String password,
                                                   @RequestParam("otp") String otp) {
        authenticationService.confirmForgotPassword(email,password,otp);
        return ResponseEntity.ok("Xác thực thành công");
    }


}

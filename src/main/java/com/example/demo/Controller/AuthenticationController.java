package com.example.demo.Controller;

import com.example.demo.DtoSecurity.*;
//import com.example.demo.Entity.ChucVu;
//import com.example.demo.Entity.TaiKhoan;
//import com.example.demo.Repo.ChucVuRepository;
import com.example.demo.Repo.CompanyRepository;
//import com.example.demo.Repo.TaiKhoanRepository;
import com.example.demo.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    // Service để xử lý các yêu cầu xác thực
    AuthenticationService authenticationService;
    PasswordEncoder passwordEncoder;
//    ChucVuRepository chucVuRepository;
//    TaiKhoanRepository taiKhoanRepository;
    CompanyRepository companyRepository;

    // Endpoint để đăng nhập và lấy JWT
    @PostMapping("/login")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticationRequest request) {
        var result = authenticationService.authenticate(request);
        return ResponseEntity.ok()
                    .header("token", result.getToken())
                    .header("cid",result.getCid().toString())
                    .body("ok");
    }


    public ResponseEntity<String> authenticate2( AuthenticationRequest authenticationRequest) {
        // Gọi service để xử lý yêu cầu đăng nhập
        var result = authenticationService.authenticate(authenticationRequest);

        // Trả về phản hồi với mã thành công và kết quả đăng nhập
        return ResponseEntity.ok()
                .header("token", result.getToken())
                .header("cid",result.getCid().toString())
                .body("ok");
    }

//    @PostMapping("/register")
//    public ResponseEntity<?> register( @RequestBody CreateUserRequest request) {
//        TaiKhoan taiKhoan = new TaiKhoan();
//
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }

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
                .header("cid",result.getCid().toString())
                .body("ok");
    }

    // Endpoint để đăng xuất và thu hồi token
    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        // Gọi service để thực hiện đăng xuất
        authenticationService.logout(request);

        return ApiResponse.<Void>builder().build();
    }

//    @GetMapping("/login-google")
//    public ResponseEntity<?> handleGoogleLoginSuccess(OAuth2AuthenticationToken authentication) {
//        Root root = toPerson(authentication.getPrincipal().getAttributes());
//        TaiKhoan taiKhoan = new TaiKhoan();
//        Company company = new Company();
//        company.setStatus(Utils.ACTIVE);
//        company = companyRepository.save(company);
//        ChucVu chucVu = chucVuRepository.getReferenceById("ADMIN");
//        var roles = new HashSet<ChucVu>();
//        roles.add(chucVu);
//
//        taiKhoan.setTenDangNhap(root.getEmail());
//        taiKhoan.setMatKhau(passwordEncoder.encode(root.getSub()));
//        taiKhoan.setTrangThai(Utils.ACTIVE);
//        taiKhoan.setChucVus(roles);
//        taiKhoan.setCompanyId(company.getId());
//        taiKhoanRepository.save(taiKhoan);
//
//        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
//        authenticationRequest.setTenDangNhap(root.getEmail());
//        authenticationRequest.setMatKhau(root.getSub());
//
//
//
//
//        return ResponseEntity.ok(authenticate2(authenticationRequest));
//    }
//    public Root toPerson(Map<String, Object>objectMap){
//        if(objectMap== null){
//            return null;
//        }
//        Root root = new Root();
//        root.setEmail((String) objectMap.get("email"));
//        root.setName((String) objectMap.get("name"));
//        root.setPicture((String) objectMap.get("picture"));
//        root.setSub((String) objectMap.get("sub"));
//    return root;
//    }

}

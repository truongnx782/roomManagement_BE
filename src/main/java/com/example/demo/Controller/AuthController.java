//package com.example.demo.Controller;
//import com.example.demo.Request.CreateUserRequest;
//import com.example.demo.Request.LoginRequest;
//import com.example.demo.Response.MessageResponse;
//import com.example.demo.Response.UserInfoResponse;
//import com.example.demo.Service.UserService;
//import com.example.demo.security.jwt.JwtUtils;
//import com.example.demo.security.service.UserDetailsImpl;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import io.swagger.v3.oas.annotations.Operation;
//import jakarta.validation.Valid;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/auth")
//@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
//public class AuthController {
//
//    private final  AuthenticationManager authenticationManager;;
//    private final JwtUtils jwtUtils;
//    private final UserDetailsService userDetailsService;
//    private final UserService userService;
//
//    public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserDetailsService userDetailsService, UserService userService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtUtils = jwtUtils;
//        this.userDetailsService = userDetailsService;
//        this.userService = userService;
//    }
//
//    @PostMapping("/login")
//    @Operation(summary = "Đăng nhập")
//    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
//        try {
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
//            );
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
//            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//
//            List<String> roles = userDetails.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok()
////                    .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
//                    .header("token", jwtCookie.toString())
//                    .header("cid", userDetails.getCompanyId().toString())
//                    .body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Login failed"));
//        }
//    }
//
//    @PostMapping("/register")
//    @Operation(summary = "Đăng ký")
//    public ResponseEntity<?> register(@Valid @RequestBody CreateUserRequest request) {
//        userService.register(request);
//        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
//    }
//
//    @GetMapping("/logout")
//    @Operation(summary = "Đăng xuất")
//    public ResponseEntity<?> logoutUser() {
//        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
//        return ResponseEntity.ok()
//                .header("token", cookie.toString())
//                .header("cid","")
//                .body(new MessageResponse("You've been logout!"));
//    }
//
//    @GetMapping("/login-google/success")
//    public ResponseEntity<?> handleGoogleLoginSuccess(OAuth2AuthenticationToken authentication) {
//        OAuth2User oAuth2User = authentication.getPrincipal();
//        System.out.println("Google User Info: " + oAuth2User.getAttributes());
//        return ResponseEntity.ok(oAuth2User.getAttributes());
//    }
//
//
//    @GetMapping("/autoLogin")
//    @Operation(summary = "Auto Login")
//    public ResponseEntity<?> autoLogin(@CookieValue(name = "token", defaultValue = "") String token) {
//        if (token.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Token is missing"));
//        }
//
//        if (jwtUtils.validateJwtToken(token)) {
//            String username = jwtUtils.getUserNameFromJwtToken(token);
//            UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
//
//            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                    userDetails, null, userDetails.getAuthorities());
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
//
//            List<String> roles = userDetails.getAuthorities().stream()
//                    .map(GrantedAuthority::getAuthority)
//                    .collect(Collectors.toList());
//
//            return ResponseEntity.ok()
////                    .header("token", jwtCookie.toString())
////                    .header("cid", userDetails.getCompanyId().toString())
//                    .body(new UserInfoResponse(userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Invalid token"));
//        }
//    }
//
//}
//

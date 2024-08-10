package com.example.demo.Service;

import com.example.demo.DTO.RootDTO;
import com.example.demo.DTO.DtoSecurity.*;
import com.example.demo.Entity.*;
import com.example.demo.Repo.*;
import com.example.demo.Util.ERole;
import com.example.demo.Util.Utils;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {
    UserRepository userRepository;
    InvalidatedTokenRepository invalidatedTokenRepository;
    RoleRepository roleRepository;
    CompanyRepository companyRepository;
    User_RoleRepository user_roleRepository;
    JavaMailSender javaMailSender;
    Map<String, String> otpCache = new ConcurrentHashMap<>();


    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var taiKhoan = userRepository.findByUsernameAndStatus(request.getUsername(), Utils.ACTIVE).orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);


        boolean authenticated = passwordEncoder.matches(request.getPassword(), taiKhoan.getPassword());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(taiKhoan);

        System.out.println(taiKhoan);

        return AuthenticationResponse.builder().token(token).cid(taiKhoan.getCompanyId()).build();
    }

    public void logout(String request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request, true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        var signedJWT = verifyToken(request.getToken(), true);

        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken =
                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

        invalidatedTokenRepository.save(invalidatedToken);

        var username = signedJWT.getJWTClaimsSet().getSubject();

        var taiKhoan =
                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

        var token = generateToken(taiKhoan);

        return AuthenticationResponse.builder().token(token).cid(taiKhoan.getCompanyId()).build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Tạo danh sách các vai trò của người dùng
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName())
                .collect(Collectors.toList());

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(user.getPassword())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .jwtID(UUID.randomUUID().toString())
                .claim("roles", roles) // Thêm vai trò vào claims
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create token", e);
            throw new RuntimeException(e);
        }
    }


    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }

    public AuthenticationRequest handleGoogleLogin(Map<String, Object> payload) {
        RootDTO root = new RootDTO();
        root.setEmail((String) payload.get("email"));
        root.setName((String) payload.get("name"));
        root.setSub((String) payload.get("sub"));
        root.setPicture((String) payload.get("picture"));

        Optional<User> user = userRepository.findByUsername(root.getEmail());
        if (user.isEmpty()) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
        Company company = new Company();
        company.setCompanyCode("COMPANY_" + root.getEmail());
        company.setCompanyName("COMPANY_" + root.getEmail());
        company.setStatus(Utils.ACTIVE);
        company = companyRepository.save(company);
        User_Role user_role = new User_Role();
        user_role.setRoleId(role.get());
        User u = new User();
        u.setUsername(root.getEmail());
        u.setPassword(passwordEncoder.encode(root.getSub()));
        u.setCompanyId(company.getId());
        u.setStatus(Utils.ACTIVE);
        u = userRepository.save(u);
        user_role.setUserId(u);
        user_role.setCompanyId(company.getId());
        user_role.setStatus(Utils.ACTIVE);
        user_roleRepository.save(user_role);
        }
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(root.getEmail());
        authenticationRequest.setPassword(root.getSub());
        return authenticationRequest;

    }

    public void register(RegisterRequest request) {
        Optional<User> user = userRepository.findByUsername(request.getEmail());
        if (user.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        } else {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            Random random = new Random();
            int randomNumber = random.nextInt(9000) + 1000;
            User u = new User();
            u.setUsername(request.getEmail());
            u.setPassword(passwordEncoder.encode(request.getPassword()) + "_" + randomNumber);
            u.setStatus(Utils.WAIT_FOR_CONFIRMATION);
            userRepository.save(u);

            // Xây dựng URL với các tham số
            String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/auth/confirm-register")
                    .queryParam("email", request.getEmail())
                    .queryParam("password", passwordEncoder.encode(request.getPassword()))
                    .queryParam("otp", randomNumber)
                    .toUriString();

            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(request.getEmail());
            msg.setSubject("Xác nhận đăng ký");
            msg.setText("XÁC NHẬN: " + url);
            javaMailSender.send(msg);
        }
    }

    public void confirmRegister(String email, String otp) {
        Optional<User> user = userRepository.findByUsername(email);
        if (!user.get().getPassword().contains(otp)) {
            throw new IllegalArgumentException("invalid");
        }
        Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
        Company company = new Company();
        company.setCompanyCode("COMPANY_" + email);
        company.setCompanyName("COMPANY_" + email);
        company.setStatus(Utils.ACTIVE);
        company = companyRepository.save(company);

        user.get().setCompanyId(company.getId());
        user.get().setStatus(Utils.ACTIVE);
        user.get().setPassword(user.get().getPassword().split("_")[0]);
        userRepository.save(user.get());

        User_Role user_role = new User_Role();
        user_role.setRoleId(role.get());
        user_role.setUserId(user.get());
        user_roleRepository.save(user_role);
    }



    public void forgotPassword(Map<String, Object> payload) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000;

        String email = (String) payload.get("email");
        String password = (String) payload.get("password");

        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email is empty or null");
        }

        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password is empty or null");
        }

        Optional<User> user = userRepository.findByUsername(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Email not found");
        }
        otpCache.put(email, String.valueOf(randomNumber));

        String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/auth/confirm-forgot-password")
                .queryParam("email", email)
                .queryParam("password", passwordEncoder.encode(password))
                .queryParam("otp", randomNumber)
                .toUriString();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Xác nhận đổi mật khẩu mới");
        msg.setText("XÁC NHẬN: " + url);
        javaMailSender.send(msg);

    }

    public void confirmForgotPassword(String email, String password, String otp) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        String storedOtp = otpCache.get(email);

        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }
        Optional<User> user = userRepository.findByUsername(email);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("Email not found");
        }

        User foundUser = user.get();
        foundUser.setPassword(password);
        userRepository.save(foundUser);
        otpCache.remove(email);
    }

    @Scheduled(fixedDelay = 300000)
    public void autoDeleteUser() {
        List<User> users = userRepository.findAllByStatus(Utils.WAIT_FOR_CONFIRMATION);
        userRepository.deleteAll(users);
        otpCache.clear();
    }

//    private String buildScope(TaiKhoan taiKhoan) {
//        StringJoiner stringJoiner = new StringJoiner(" ");
//
//        if (!CollectionUtils.isEmpty(taiKhoan.getChucVus()))
//            taiKhoan.getChucVus().forEach(role -> {
//                stringJoiner.add("ROLE_" + role.getName());
//                if (!CollectionUtils.isEmpty(role.getPermissions()))
//                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
//            });
//
//        return stringJoiner.toString();
//    }
}

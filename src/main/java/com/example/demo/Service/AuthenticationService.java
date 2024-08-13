package com.example.demo.Service;

import com.example.demo.DTO.RootDTO;
import com.example.demo.DTO.DtoSecurity.*;
import com.example.demo.Entity.*;
import com.example.demo.Repo.*;
import com.example.demo.Util.ERole;
import com.example.demo.Util.Utils;
import com.example.demo.configuration.CustomJwtDecoder;
import com.example.demo.exception.AppException;
import com.example.demo.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
public class AuthenticationService {
    private final UserRepository userRepository;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;
    private final User_RoleRepository user_roleRepository;
    private final JavaMailSender javaMailSender;
    private final Map<String, String> otpCache = new ConcurrentHashMap<>();

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @Value("${jwt.valid-duration}")
    private long VALID_DURATION;

    @Value("${jwt.refreshable-duration}")
    private long REFRESHABLE_DURATION;

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

    public void register(RegisterRequest request) throws MessagingException {
        Optional<User> user = userRepository.findByUsername(request.getEmail());
        if (user.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        } else {

            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
            Random random = new Random();
            int randomNumber = random.nextInt(9000) + 1000;
            otpCache.put(request.getEmail(), String.valueOf(randomNumber));

            // Xây dựng URL với các tham số
            String url = UriComponentsBuilder.fromHttpUrl("http://localhost:8080/api/auth/confirm-register")
                    .queryParam("email", request.getEmail())
                    .queryParam("password", passwordEncoder.encode(request.getPassword()))
                    .queryParam("otp", randomNumber)
                    .toUriString();

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(request.getEmail());
            helper.setSubject("Xác nhận đăng kí tài khảon");

            // HTML content with the icon as a clickable link
            String htmlContent = "<html>"
                    + "<body>"
                    + "<p style=\"display: inline; margin-right: 10px;\">XÁC NHẬN:</p>"
                    + "<a href=\"" + url + "\" style=\"display: inline;\">"
                    + "<img src=\"https://media.istockphoto.com/id/1080145334/vector/icon-quality.jpg?s=612x612&w=0&k=20&c=8BbbL_TXaH0oxnjiqWKrQSiqUlBC1S4E9HCRc1ZtNGs=\" alt=\"Confirm Password\" style=\"width:50px;height:50px;\">"
                    + "</a>"
                    + "</body>"
                    + "</html>";

            helper.setText(htmlContent, true);
            javaMailSender.send(message);
        }
    }

    public void confirmRegister(String email,String password, String otp) {
        String storedOtp = otpCache.get(email);
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new IllegalArgumentException("Invalid OTP");
        }

        Optional<User> user = userRepository.findByUsername(email);
        if (user.isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        User u = new User();

        Optional<Role> role = roleRepository.findByName(ERole.ROLE_ADMIN);
        Company company = new Company();
        company.setCompanyCode("COMPANY_" + email);
        company.setCompanyName("COMPANY_" + email);
        company.setStatus(Utils.ACTIVE);
        company = companyRepository.save(company);

        u.setUsername(email);
        u.setStatus(Utils.ACTIVE);
        u.setPassword(password);
        u.setCompanyId(company.getId());
        u=userRepository.save(u);

        User_Role user_role = new User_Role();
        user_role.setRoleId(role.get());
        user_role.setUserId(u);
        user_role.setCompanyId(company.getId());
        user_role.setStatus(Utils.ACTIVE);
        user_roleRepository.save(user_role);
    }


    public void forgotPassword(Map<String, Object> payload) throws MessagingException {
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

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(email);
        helper.setSubject("Xác nhận đổi mật khẩu mới");

        // HTML content with the icon as a clickable link
        String htmlContent = "<html>"
                + "<body>"
                + "<p style=\"display: inline; margin-right: 10px;\">XÁC NHẬN:</p>"
                + "<a href=\"" + url + "\" style=\"display: inline;\">"
                + "<img src=\"https://media.istockphoto.com/id/1080145334/vector/icon-quality.jpg?s=612x612&w=0&k=20&c=8BbbL_TXaH0oxnjiqWKrQSiqUlBC1S4E9HCRc1ZtNGs=\" alt=\"Confirm Password\" style=\"width:50px;height:50px;\">"
                + "</a>"
                + "</body>"
                + "</html>";

        helper.setText(htmlContent, true);
        javaMailSender.send(message);
    }

    public void confirmForgotPassword(String email, String password, String otp) {
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



    public Map<String, Object> validateToken(String token) throws ParseException, JOSEException {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("Invalid token");
        }
        // Remove the "Bearer " prefix if present
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        // Verify and parse the token
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        if (!signedJWT.verify(verifier)) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // Get 'sub' from JWT claims set
        String username = signedJWT.getJWTClaimsSet().getSubject();

        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isEmpty()) {
            throw new AppException(ErrorCode.ACCOUNT_NOT_EXISTED);
        }

        User user = userOptional.get();

        // Create map to return token and cid
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("cid", user.getCompanyId().toString());

        return response;
    }

//    public AuthenticationResponse refreshToken(String request) throws ParseException, JOSEException {
//        var signedJWT = verifyToken(request, true);
//
//        var jit = signedJWT.getJWTClaimsSet().getJWTID();
//        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
//
//        InvalidatedToken invalidatedToken =
//                InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();
//
//        invalidatedTokenRepository.save(invalidatedToken);
//
//        var username = signedJWT.getJWTClaimsSet().getSubject();
//
//        var taiKhoan =
//                userRepository.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));
//
//        var token = generateToken(taiKhoan);
//
//        return AuthenticationResponse.builder().token(token).cid(taiKhoan.getCompanyId()).build();
//    }

public AuthenticationResponse refreshToken(String token) throws ParseException, JOSEException {
    if (token == null || token.isEmpty()) {
        throw new IllegalArgumentException("Invalid token");
    }

    // Remove the "Bearer " prefix if present
    if (token.startsWith("Bearer ")) {
        token = token.substring(7);
    }

    // Verify and parse the token
    SignedJWT signedJWT = SignedJWT.parse(token);

    // Verify the token and check its expiration
    verifyToken(token, true);

    // Generate a new token
    User user = userRepository.findByUsername(signedJWT.getJWTClaimsSet().getSubject())
            .orElseThrow(() -> new AppException(ErrorCode.ACCOUNT_NOT_EXISTED));

    // Generate a new token with updated expiration
    String newToken = generateToken(user);

    return AuthenticationResponse.builder()
            .token(newToken)
            .cid(user.getCompanyId())
            .build();
}



    @Scheduled(fixedDelay = 300000)
    public void autoDeleteUser() {
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

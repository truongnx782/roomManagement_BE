package com.example.demo.configuration;

import com.example.demo.DtoSecurity.IntrospectRequest;
import com.example.demo.Service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.signerKey}")
    private String signerKey; // Khoá bí mật để mã hóa/giải mã JWT

    @Autowired
    private AuthenticationService authenticationService; // Dịch vụ kiểm tra tính hợp lệ của JWT

    private NimbusJwtDecoder nimbusJwtDecoder = null; // Đối tượng NimbusJwtDecoder

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Gọi dịch vụ để kiểm tra tính hợp lệ của token
            var response = authenticationService.introspect(
                    IntrospectRequest.builder().token(token).build());

            // Nếu token không hợp lệ, ném ra ngoại lệ JwtException
            if (!response.isValid()) throw new JwtException("Token invalid");
        } catch (JOSEException | ParseException e) {
            // Xử lý lỗi khi kiểm tra token
            throw new JwtException(e.getMessage());
        }

        // Nếu NimbusJwtDecoder chưa được khởi tạo, khởi tạo nó
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        // Giải mã token và trả về đối tượng Jwt
        return nimbusJwtDecoder.decode(token);
    }
}

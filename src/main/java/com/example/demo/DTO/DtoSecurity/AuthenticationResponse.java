package com.example.demo.DTO.DtoSecurity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    BigInteger cid;
}
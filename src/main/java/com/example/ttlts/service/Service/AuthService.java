package com.example.ttlts.service.Service;

import com.example.ttlts.dto.request.AuthenticationRequest;
import com.example.ttlts.dto.response.AuthenticationResponse;
import com.example.ttlts.entity.RefreshToken;
import com.example.ttlts.entity.User;
import com.example.ttlts.exception.AppException;
import com.example.ttlts.exception.ErrException;
import com.example.ttlts.repository.RefeshTokenRepository;
import com.example.ttlts.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RefeshTokenRepository refeshTokenRepository;;

    protected static final String KEY_SIGN = "lQgnbki8rjdh62RZ2FNXZB9KWYB1IjajiY04z011BXjjagnc7a";

    final JwtDecoder jwtDecoder;

    public AuthService(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    public String decodeToken(String token) {
        RefreshToken refreshToken = refeshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token not found"));
        if (refreshToken.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token", e);
        }
    }


    String createToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256); // xac dinh header cua token
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder() // noidung gui di cua token
                .subject(user.getUsername())
                .issuer("dat2003") // dinh dang nguoi tao ra token
                .issueTime(new Date()) // tgian tao hien tai
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() // thoi han dung
                ))
                .claim("scope","Admin") // get role
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject()); // xac dinh thong tin cua token
        JWSObject jwsObject = new JWSObject(header, payload); // xac dinh token

        try {
            jwsObject.sign(new MACSigner(KEY_SIGN.getBytes())); // xac dinh signature cua token
            return jwsObject.serialize(); // tra ve token da duoc ky
        } catch (JOSEException e) {
            throw new RuntimeException();
        }
    }


    public AuthenticationResponse authenticate(AuthenticationRequest authRequest) {
        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = createToken(user);
        return new AuthenticationResponse(token, true);
    }
}


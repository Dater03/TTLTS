package com.example.ttlts.service.Service;

import com.example.ttlts.dto.request.AuthenticationRequest;
import com.example.ttlts.dto.response.AuthenticationResponse;
import com.example.ttlts.entity.User;
import com.example.ttlts.exception.AppException;
import com.example.ttlts.exception.ErrException;
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
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    protected static final String KEY_SIGN = "lQgnbki8rjdh62RZ2FNXZB9KWYB1IjajiY04z011BXjjagnc7a";

    JwtDecoder jwtDecoder;

    public String decodeToken(String token) {
        try {
            Jwt jwt = jwtDecoder.decode(token);
            return jwt.getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid token");
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

    public AuthenticationResponse authenticationResponse(AuthenticationRequest authenticationRequest){
        var user = userRepository.findByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new AppException((ErrException.USER_NOT_EXISTED)));
        boolean checked = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
        if(!checked){
            throw new AppException(ErrException.USER_NOT_EXISTED);
        }
        var token = createToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .check(true)
                .build();
    }
}


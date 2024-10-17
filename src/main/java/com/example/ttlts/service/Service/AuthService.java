package com.example.ttlts.service.Service;

import com.example.ttlts.dto.request.AuthenticationRequest;
import com.example.ttlts.dto.response.AuthenticationResponse;
import com.example.ttlts.entity.RefreshToken;
import com.example.ttlts.entity.Role;
import com.example.ttlts.entity.User;
import com.example.ttlts.exception.AppException;
import com.example.ttlts.exception.ErrException;
import com.example.ttlts.repository.RefeshTokenRepository;
import com.example.ttlts.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AuthService {
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;
    RefreshTokenService refreshTokenService;;

    protected static final String KEY_SIGN = "lQgnbki8rjdh62RZ2FNXZB9KWYB1IjajiY04z011BXjjagnc7a";

    public String createToken(User user){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256); // xac dinh header cua token
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder() // noidung gui di cua token
                .subject(user.getUsername())
                .issuer("dat2003") // dinh dang nguoi tao ra token
                .issueTime(new Date()) // tgian tao hien tai
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli() // thoi han dung
                ))
                .claim("scope",buildScopeToRoles(user)) // get role
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
        refreshTokenService.saveToken(user, token);
        return AuthenticationResponse.builder()
                .token(token)
                .check(true)
                .build();
    }

    public String buildScopeToRoles(User user) {
        StringJoiner stringJoiner = new StringJoiner(" "); // tạo một chuỗi với các role cách nhau bằng khoảng trắng

        if (!CollectionUtils.isEmpty(user.getPermissions())) {
            user.getPermissions().forEach(permission -> {
                Role role = permission.getRole(); // lấy role từ permission
                if (role != null) {
                    stringJoiner.add(role.getRoleName()); // thêm tên vai trò vào chuỗi
                }
            });
        }

        return stringJoiner.toString(); // ví dụ: nếu có các vai trò "ADMIN", "USER", thì trả về chuỗi: "ADMIN USER"
    }



}


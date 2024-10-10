package com.example.ttlts.controller;

import com.example.ttlts.dto.request.AuthenticationRequest;
import com.example.ttlts.dto.response.ApiResponse;
import com.example.ttlts.dto.response.AuthenticationResponse;
import com.example.ttlts.service.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authen")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationController {
    AuthService authenticationService;
    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> createToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticationResponse(authenticationRequest))
                .code(200)
                .build();
    }
}

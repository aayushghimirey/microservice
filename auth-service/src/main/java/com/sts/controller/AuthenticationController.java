package com.sts.controller;


import com.sts.dto.request.AuthRequest;
import com.sts.dto.response.AuthResponse;
import com.sts.response.ApiResponse;
import com.sts.response.AppResponse;
import com.sts.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/public")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> authenticate(@RequestBody AuthRequest request) {
        return AppResponse.success(authenticationService.authenticate(request), "Login successful");
    }

}

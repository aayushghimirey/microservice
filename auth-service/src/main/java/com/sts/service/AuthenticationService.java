package com.sts.service;


import com.sts.dto.request.AuthRequest;
import com.sts.dto.response.AuthResponse;
import com.sts.mapper.AuthMapper;
import com.sts.model.User;
import com.sts.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/*
 * Handle all user login
 * staff, admin and super admin
 * */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthMapper authMapper;
    private final SuperAdminService superAdminService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(AuthRequest request) {

        final User superAdmin = superAdminService.validateSuperAdminLogin(request.username(), request.password());

        if (superAdmin != null) {
            return authMapper.toAuthResDto(superAdmin, jwtService.generateToken(superAdmin));
        }

        final User user = userRepository.findByEmail(request.username())
                .or(() -> userRepository.findByPhoneNumber(request.username()))
                .or(() -> userRepository.findByUsername(request.username()))
                .orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), request.password()));

        return authMapper.toAuthResDto(user, jwtService.generateToken(user));

    }
}

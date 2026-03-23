package com.sts.mapper;

import com.sts.dto.response.AuthResponse;
import com.sts.model.User;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {

    public AuthResponse toAuthResDto(User user, String token) {
        return AuthResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .token(token)
                .build();
    }

}

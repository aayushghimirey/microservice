package com.sts.service;


import com.sts.model.User;
import com.sts.utils.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class SuperAdminService {

    private final String superAdminUsername = "superadmin";
    private final String superAdminPassword = "superadmin";

    public User validateSuperAdminLogin(String username, String password) {
        if (superAdminUsername.equals(username) && superAdminPassword.equals(password)) {
            return User.builder()
                    .username(superAdminUsername)
                    .role(UserRole.SUPER_ADMIN)
                    .build();
        }
        return null;
    }
}

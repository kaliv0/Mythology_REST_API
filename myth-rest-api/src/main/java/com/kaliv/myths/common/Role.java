package com.kaliv.myths.common;

import static com.kaliv.myths.constant.security.Authority.ADMIN_AUTHORITIES;
import static com.kaliv.myths.constant.security.Authority.MANAGER_AUTHORITIES;
import static com.kaliv.myths.constant.security.Authority.USER_AUTHORITIES;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_MANAGER(MANAGER_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    public final String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}

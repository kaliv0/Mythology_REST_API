package com.kaliv.myths.entity.user;

import static com.kaliv.myths.constant.AuthorityConstants.ADMIN_AUTHORITIES;
import static com.kaliv.myths.constant.AuthorityConstants.STAFF_AUTHORITIES;
import static com.kaliv.myths.constant.AuthorityConstants.USER_AUTHORITIES;

public enum Role {
    ROLE_USER(USER_AUTHORITIES),
    ROLE_STAFF(STAFF_AUTHORITIES),
    ROLE_ADMIN(ADMIN_AUTHORITIES);

    public final String[] authorities;

    Role(String... authorities) {
        this.authorities = authorities;
    }

    public String[] getAuthorities() {
        return authorities;
    }
}

package com.kaliv.myths.dto.userDtos;

import javax.persistence.Enumerated;

import com.kaliv.myths.entity.user.Role;
import com.kaliv.myths.util.validator.role.ValidRole;

import jdk.jfr.BooleanFlag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AddUserDto extends RegisterUserDto {
    @Enumerated
    @ValidRole(anyOf = {Role.ROLE_USER, Role.ROLE_MANAGER})
    Role role;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @BooleanFlag
    boolean isActive;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @BooleanFlag
    boolean isNotLocked;


    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }
}

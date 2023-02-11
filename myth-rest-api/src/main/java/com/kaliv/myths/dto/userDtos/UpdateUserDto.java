package com.kaliv.myths.dto.userDtos;

import javax.persistence.Enumerated;

import com.kaliv.myths.constant.types.RoleType;
import com.kaliv.myths.util.validator.role.ValidRole;

import jdk.jfr.BooleanFlag;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateUserDto extends UpdateUserProfileDto {
    @Enumerated
    @ValidRole(anyOf = {RoleType.USER, RoleType.STAFF, RoleType.ADMIN})
    RoleType role;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @BooleanFlag
    Boolean isActive;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @BooleanFlag
    Boolean isNotLocked;

    public Boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Boolean isNotLocked() {
        return isNotLocked;
    }

    public void setNotLocked(boolean notLocked) {
        isNotLocked = notLocked;
    }

}

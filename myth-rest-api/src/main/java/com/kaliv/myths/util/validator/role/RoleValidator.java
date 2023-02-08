package com.kaliv.myths.util.validator.role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;

import com.kaliv.myths.entity.users.Role;

public class RoleValidator implements ConstraintValidator<ValidRole, Role> {
    private Role[] subset;

    @Override
    public void initialize(ValidRole constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(Role value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}

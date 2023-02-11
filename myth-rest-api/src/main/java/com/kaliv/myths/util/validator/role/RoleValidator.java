package com.kaliv.myths.util.validator.role;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;

import com.kaliv.myths.constant.types.RoleType;

public class RoleValidator implements ConstraintValidator<ValidRole, RoleType> {
    private RoleType[] subset;

    @Override
    public void initialize(ValidRole constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(RoleType value, ConstraintValidatorContext context) {
        return value == null || Arrays.asList(subset).contains(value);
    }
}

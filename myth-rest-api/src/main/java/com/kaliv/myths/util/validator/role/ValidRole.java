package com.kaliv.myths.util.validator.role;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

import com.kaliv.myths.entity.users.Role;


@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface ValidRole {
    Role[] anyOf();

    String message() default "Added user role must be any of {anyOf}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

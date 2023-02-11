package com.kaliv.myths.util.validator.role;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

import com.kaliv.myths.entity.users.RoleType;

import static com.kaliv.myths.constant.messages.ExceptionMessages.INVALID_USER_ROLE;


@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RoleValidator.class)
public @interface ValidRole {
    RoleType[] anyOf();

    String message() default INVALID_USER_ROLE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

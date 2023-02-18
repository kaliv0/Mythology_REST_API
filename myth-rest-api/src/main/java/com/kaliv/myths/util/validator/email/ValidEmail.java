package com.kaliv.myths.util.validator.email;


import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

import static com.kaliv.myths.constant.messages.ExceptionMessages.INVALID_EMAIL;

@Target({ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default INVALID_EMAIL;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
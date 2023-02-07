package com.kaliv.myths.common.validator.file;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.*;

import static com.kaliv.myths.constant.messages.ExceptionMessages.INVALID_FILE_TYPE;

@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {FileValidator.class})
public @interface ValidFile {

    String message() default INVALID_FILE_TYPE;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
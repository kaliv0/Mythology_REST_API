package com.kaliv.myths.util.validator.email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.querydsl.core.util.StringUtils;

import static com.kaliv.myths.constant.EmailConstants.VALID_EMAIL_PATTERN;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
    private static final Pattern PATTERN = Pattern.compile(VALID_EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(final String email, final ConstraintValidatorContext context) {
        if (StringUtils.isNullOrEmpty(email)) {
            return true;
        }
        return validateEmail(email);
    }

    private boolean validateEmail(final String email) {
        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();
    }
}

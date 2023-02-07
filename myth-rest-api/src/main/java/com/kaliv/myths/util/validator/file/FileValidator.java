package com.kaliv.myths.util.validator.file;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Arrays;

import org.springframework.web.multipart.MultipartFile;

import static com.kaliv.myths.constant.ImageContentType.SUPPORTED_CONTENT_TYPES;


public class FileValidator implements ConstraintValidator<ValidFile, MultipartFile> {

    @Override
    public void initialize(ValidFile constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
        String contentType = multipartFile.getContentType();
        return isSupportedContentType(contentType);
    }

    private boolean isSupportedContentType(String contentType) {
        return Arrays.asList(SUPPORTED_CONTENT_TYPES).contains(contentType);
    }
}

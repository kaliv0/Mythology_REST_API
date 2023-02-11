package com.kaliv.myths.constant.types;

import org.springframework.http.MediaType;

public class ImageContentType {
    public static final String JPEG = "jpg";
    public static final String PNG = "png";
    public static final String GIF = "gif";

    public static final String[] SUPPORTED_CONTENT_TYPES = {
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE
    };
}

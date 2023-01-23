package com.kaliv.myths.dto.imageDtos;

import com.kaliv.myths.dto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UploadImageResponseDto extends BaseDto {
    private String message;
}

package com.kaliv.myths.dto.imageDtos;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.kaliv.myths.dto.BaseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@MappedSuperclass
public abstract class ImageDetailsDto extends BaseDto {
    private String type;

    @Lob
    private byte[] imageData;
}

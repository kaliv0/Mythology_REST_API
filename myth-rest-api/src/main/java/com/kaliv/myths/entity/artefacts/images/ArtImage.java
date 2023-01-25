package com.kaliv.myths.entity.artefacts.images;

import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;

import com.kaliv.myths.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@MappedSuperclass
public abstract class ArtImage extends BaseEntity {
    //TODO: could be JPEG, PNG, GIF
    private String type;

    @Lob
    private byte[] imageData;
}

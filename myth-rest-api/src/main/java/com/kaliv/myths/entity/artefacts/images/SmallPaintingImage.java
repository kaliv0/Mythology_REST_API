package com.kaliv.myths.entity.artefacts.images;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kaliv.myths.entity.artefacts.Painting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "small_painting_images")
public class SmallPaintingImage extends ArtImage {
    @ManyToOne
    @JoinColumn(name = "small_painting_id", referencedColumnName = "id")
    private Painting painting;
}

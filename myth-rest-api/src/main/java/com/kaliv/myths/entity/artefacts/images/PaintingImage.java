package com.kaliv.myths.entity.artefacts.images;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kaliv.myths.entity.artefacts.Painting;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "painting_images")
public class PaintingImage extends Image {
    @ManyToOne
    @JoinColumn(name = "painting_id", referencedColumnName = "id")
    private Painting painting;
}

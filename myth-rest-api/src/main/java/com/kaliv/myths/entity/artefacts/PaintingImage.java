package com.kaliv.myths.entity.artefacts;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "painting_images")
public class PaintingImage extends Image{
    @ManyToOne
    @JoinColumn(name = "painting_id", referencedColumnName = "id")
    private Painting painting;
}

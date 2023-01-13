package com.kaliv.myths.entity.artefacts;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class VisualArtwork extends Artwork {
    @ManyToOne
    @JoinColumn(name = "museum_id", referencedColumnName = "id")
    private Museum museum;

//    private String image_url;
}

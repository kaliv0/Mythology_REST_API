package com.kaliv.myths.entity.artefacts;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class VisualArtwork extends Artwork {
    @ManyToOne
    @JoinColumn(name = "museum_id", referencedColumnName = "id")
    private Museum museum;
}

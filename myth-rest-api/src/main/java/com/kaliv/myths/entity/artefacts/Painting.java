package com.kaliv.myths.entity.artefacts;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.artefacts.images.PaintingImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "paintings")
public class Painting extends VisualArtwork {
    @OneToMany(mappedBy = "painting", fetch = FetchType.LAZY)
    private Set<PaintingImage> paintingImages = new HashSet<>();
}

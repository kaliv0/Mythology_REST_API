package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.artefacts.images.SmallStatueImage;
import com.kaliv.myths.entity.artefacts.images.StatueImage;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "statues")
public class Statue extends VisualArtwork {
    @OneToMany(mappedBy = "statue", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<StatueImage> statueImages = new HashSet<>();

    @OneToMany(mappedBy = "statue", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Set<SmallStatueImage> smallStatueImages = new HashSet<>();
}

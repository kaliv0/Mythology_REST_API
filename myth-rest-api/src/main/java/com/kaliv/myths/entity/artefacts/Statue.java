package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "statues")
public class Statue extends VisualArtwork {
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "statues_images",
            joinColumns = @JoinColumn(
                    name = "statue_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "image_id", referencedColumnName = "id"
            )
    )
    private Set<Image> images = new HashSet<>();
}

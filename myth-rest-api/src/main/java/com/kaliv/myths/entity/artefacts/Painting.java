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
@Table(name = "paintings")
public class Painting extends VisualArtwork {

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "paintings_images",
            joinColumns = @JoinColumn(
                    name = "painting_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "image_id", referencedColumnName = "id"
            )
    )
    private Set<Image> images = new HashSet<>();
}

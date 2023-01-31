package com.kaliv.myths.entity.artefacts;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.artefacts.contracts.possessors.VisualArtworkPossessor;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "museums")
public class Museum extends VisualArtworkPossessor {
    @OneToMany(mappedBy = "museum")
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(mappedBy = "museum")
    private Set<Painting> paintings = new HashSet<>();
}

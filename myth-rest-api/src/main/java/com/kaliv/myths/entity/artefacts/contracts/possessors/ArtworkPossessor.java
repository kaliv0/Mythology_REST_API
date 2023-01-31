package com.kaliv.myths.entity.artefacts.contracts.possessors;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import java.util.Set;

import com.kaliv.myths.entity.artefacts.Music;
import com.kaliv.myths.entity.artefacts.Poem;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class ArtworkPossessor extends VisualArtworkPossessor {
    @Transient
    private Set<Music> music;
    @Transient
    private Set<Poem> poems;
}

package com.kaliv.myths.entity.artefacts;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class ArtworkPossessor extends BaseEntity {
    @Transient
    private Set<Statue> statues = new HashSet<>();

    @Transient
    private Set<Painting> paintings = new HashSet<>();

    @Transient
    private Set<Music> music = new HashSet<>();

    @Transient
    private Set<Poem> poems = new HashSet<>();
}

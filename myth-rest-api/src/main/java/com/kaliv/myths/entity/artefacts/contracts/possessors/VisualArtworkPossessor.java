package com.kaliv.myths.entity.artefacts.contracts.possessors;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import java.util.Set;

import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.artefacts.Painting;
import com.kaliv.myths.entity.artefacts.Statue;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class VisualArtworkPossessor extends BaseEntity {
    @Transient
    private Set<Statue> statues;
    @Transient
    private Set<Painting> paintings;
}

package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.MythCharacter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "poems")
public class Poem extends Artwork {
    @Column(name = "full_text_url", nullable = false)
    private String fullTextUrl;

    private String excerpt;

    @ManyToMany(mappedBy = "poems", fetch = FetchType.LAZY)
    private Set<MythCharacter> mythCharacters = new HashSet<>();
}

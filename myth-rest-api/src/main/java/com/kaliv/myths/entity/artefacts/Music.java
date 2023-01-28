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
@Table(name = "music")
public class Music extends Artwork {
    @Column(name = "recording_url", nullable = false)
    private String recordingUrl;

    @ManyToMany(mappedBy = "music", fetch = FetchType.LAZY)
    private Set<MythCharacter> mythCharacters = new HashSet<>();
}

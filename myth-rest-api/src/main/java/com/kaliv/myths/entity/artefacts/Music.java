package com.kaliv.myths.entity.artefacts;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "music")
public class Music extends Artwork {
    //TODO: how long should the url be??
    @Column(name = "recording_url", nullable = false)
    private String recordingUrl;
}

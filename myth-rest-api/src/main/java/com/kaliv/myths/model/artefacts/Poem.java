package com.kaliv.myths.model.artefacts;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "poems")
public class Poem extends Artwork {

    @Column(name = "full_text_url", nullable = false)
    private String fullTextUrl;

    private String excerpt;
}

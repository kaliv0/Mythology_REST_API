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
@Table(name = "poems")
public class Poem extends Artwork {
    //TODO: how long should the url be??
    @Column(name = "full_text_url", nullable = false)
    private String fullTextUrl;

    //TODO: how long should it be??
    private String excerpt;
}

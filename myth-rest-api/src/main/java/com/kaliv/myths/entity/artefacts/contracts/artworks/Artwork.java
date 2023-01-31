package com.kaliv.myths.entity.artefacts.contracts.artworks;

import javax.persistence.*;

import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.artefacts.Author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
public abstract class Artwork extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    /*
       One could assign a statue to a certain god or hero without specifying a myth.
       On the other hand a statue of two or more characters most probably represents a particular myth.
    */
    @ManyToOne
    @JoinColumn(name = "myth_id", referencedColumnName = "id")
    private Myth myth;
}

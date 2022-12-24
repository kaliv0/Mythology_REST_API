package com.kaliv.myths.model.artefacts;

import com.kaliv.myths.model.MythCharacter;
import com.kaliv.myths.model.Myth;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@MappedSuperclass
public abstract class Artwork {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(cascade = CascadeType.ALL) //TODO: check cascade type
    private Author author;

    /*
       One could assign a statue to a certain god or hero without specifying a myth.
       On the other hand a statue of two or more characters most probably represents a particular myth.
    */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "myth_id", referencedColumnName = "id")
    private Myth myth;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<MythCharacter> mythCharacters = new HashSet<>();
}

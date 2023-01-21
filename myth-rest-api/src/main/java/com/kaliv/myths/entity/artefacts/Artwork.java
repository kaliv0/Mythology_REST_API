package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
public abstract class Artwork extends BaseEntity {
    @ManyToOne(cascade = CascadeType.ALL) //TODO: check cascade type
    private Author author;

    /*
       One could assign a statue to a certain god or hero without specifying a myth.
       On the other hand a statue of two or more characters most probably represents a particular myth.
    */
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "myth_id", referencedColumnName = "id")
    private Myth myth;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<MythCharacter> mythCharacters = new HashSet<>();
}

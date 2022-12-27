package com.kaliv.myths.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "characters")
public class MythCharacter extends BaseEntity {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "father_id", referencedColumnName = "id")
    private MythCharacter father;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mother_id", referencedColumnName = "id")
    private MythCharacter mother;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "mythCharacters")
    private Set<Myth> myths = new HashSet<>();
}

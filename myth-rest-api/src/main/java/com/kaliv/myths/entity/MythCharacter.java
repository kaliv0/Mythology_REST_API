package com.kaliv.myths.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "characters")
public class MythCharacter extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne
    @JoinColumn(name = "father_id", referencedColumnName = "id")
    private MythCharacter father;

    @OneToOne
    @JoinColumn(name = "mother_id", referencedColumnName = "id")
    private MythCharacter mother;

    @ManyToMany(mappedBy = "mythCharacters")
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private Set<Myth> myths = new HashSet<>();
}

package com.kaliv.myths.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "characters")
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "father_id", referencedColumnName = "id")
    private Character father;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "mother_id", referencedColumnName = "id")
    private Character mother;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "characters")
    private Set<Myth> myths = new HashSet<>();
}

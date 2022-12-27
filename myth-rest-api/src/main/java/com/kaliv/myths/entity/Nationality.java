package com.kaliv.myths.entity;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.artefacts.Author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "nationalities")
public class Nationality extends BaseEntity {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Set<Myth> myths = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "nationality")
    private Set<Author> authors = new HashSet<>();
}

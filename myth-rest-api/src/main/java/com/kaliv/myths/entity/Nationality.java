package com.kaliv.myths.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    @OneToMany(mappedBy = "nationality")
    private Set<Myth> myths = new HashSet<>();

    @OneToMany(mappedBy = "nationality")
    private Set<Author> authors = new HashSet<>();
}

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
    @OneToMany(mappedBy = "nationality", fetch = FetchType.LAZY)
    private Set<Myth> myths = new HashSet<>();

    @OneToMany(mappedBy = "nationality", fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    @PrePersist
    public void addNationality() {
        this.getMyths()
                .forEach(author -> author.setNationality(this));
        this.getAuthors()
                .forEach(author -> author.setNationality(this));
    }

    @PreRemove
    public void deleteNationality() {
        this.getMyths()
                .forEach(author -> author.setNationality(null));
        this.getAuthors()
                .forEach(author -> author.setNationality(null));
    }
}

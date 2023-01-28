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
@Table(name = "character_categories")
public class Category extends BaseEntity {
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<MythCharacter> mythCharacters = new HashSet<>();

    @PrePersist
    public void addCategory() {
        this.getMythCharacters()
                .forEach(character -> character.setCategory(this));
    }

    @PreRemove
    public void deleteCategory() {
        this.getMythCharacters()
                .forEach(character -> character.setCategory(null));
    }
}

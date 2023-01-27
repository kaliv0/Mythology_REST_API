package com.kaliv.myths.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
}

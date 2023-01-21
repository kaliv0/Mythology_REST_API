package com.kaliv.myths.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<MythCharacter> mythCharacters;
}

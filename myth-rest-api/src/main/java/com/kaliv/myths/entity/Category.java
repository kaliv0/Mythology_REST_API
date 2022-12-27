package com.kaliv.myths.entity;

import javax.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "character_categories")
public class Category extends BaseEntity {
    @OneToOne(mappedBy = "category")
    private MythCharacter mythCharacter;
}

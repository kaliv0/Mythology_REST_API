package com.kaliv.myths.entity.artefacts;


import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

import com.kaliv.myths.entity.BaseEntity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
//@Table(name = "images", uniqueConstraints = @UniqueConstraint(columnNames = "image_url"))
@Table(name = "images")
@AttributeOverride(name = "name", column = @Column(name = "name", nullable = false))
public class Image extends BaseEntity {
    @Column(name = "image_url", nullable = false, unique = true)
    private String imageUrl;

    //TODO: add blob


    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "images")
    private Set<Painting> paintings = new HashSet<>();

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "images")
    private Set<Statue> statues = new HashSet<>();
}

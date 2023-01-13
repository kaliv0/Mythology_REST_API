package com.kaliv.myths.entity.artefacts;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

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
@Table(name = "museums")
public class Museum extends BaseEntity {
    private String image_url;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "museum")
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "museum")
    private Set<Painting> paintings = new HashSet<>();
}

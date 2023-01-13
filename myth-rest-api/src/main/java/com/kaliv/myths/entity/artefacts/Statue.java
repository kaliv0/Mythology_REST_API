package com.kaliv.myths.entity.artefacts;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
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
@Table(name = "statues")
public class Statue extends VisualArtwork {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "statue")
    private Set<StatueImage> images = new HashSet<>();
}

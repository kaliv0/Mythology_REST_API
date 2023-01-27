package com.kaliv.myths.entity.artefacts;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @OneToMany(mappedBy = "museum", fetch = FetchType.LAZY)
    private Set<Statue> statues = new HashSet<>();

    @OneToMany(mappedBy = "museum", fetch = FetchType.LAZY)
    private Set<Painting> paintings = new HashSet<>();
}

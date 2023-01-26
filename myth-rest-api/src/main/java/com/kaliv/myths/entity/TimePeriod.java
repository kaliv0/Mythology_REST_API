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
@Table(name = "time_periods")
public class TimePeriod extends BaseEntity {
    private String years;

    @OneToMany(mappedBy = "timePeriod")
    private Set<Author> authors = new HashSet<>();
}

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
@Table(name = "time_periods")
public class TimePeriod extends BaseEntity {
    //TODO: decide for length => could be changed to startDate:endDate (DateTime)
    private String years;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "timePeriod")
    private Set<Author> authors = new HashSet<>();
}

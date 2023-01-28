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
    private String years;

    @OneToMany(mappedBy = "timePeriod", fetch = FetchType.LAZY)
    private Set<Author> authors = new HashSet<>();

    @PrePersist
    public void addTimePeriod() {
        this.getAuthors()
                .forEach(author -> author.setTimePeriod(this));
    }

    @PreRemove
    public void deleteTimePeriod() {
        this.getAuthors()
                .forEach(author -> author.setTimePeriod(null));
    }
}

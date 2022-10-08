package com.kaliv.myths.model.artefacts;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "time_period_id", referencedColumnName = "id")
    private TimePeriod timePeriod;
}

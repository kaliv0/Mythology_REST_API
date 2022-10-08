package com.kaliv.myths.model.artefacts;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "time_periods")
public class TimePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    private String years;

    @OneToOne(mappedBy = "timePeriod")
    private Author author;
}

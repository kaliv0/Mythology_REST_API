package com.kaliv.myths.model;

import javax.persistence.*;

import com.kaliv.myths.model.artefacts.Author;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_periods")
public class TimePeriod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name="id")
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    private String years;


    //TODO: refactor to oneToMany
//    @MapsId
//    @OneToOne(mappedBy = "timePeriod")
//    @JoinColumn(name = "id")
//    private Author author;
}

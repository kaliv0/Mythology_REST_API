package com.kaliv.myths.model.artefacts;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id") //TODO:check if necessary and names to all columns
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;


    //TODO: refactor to manyToOne
//    @OneToOne(cascade = CascadeType.ALL)
////    @JoinColumn(name = "time_period_id", referencedColumnName = "id")
//    @PrimaryKeyJoinColumn
//    private TimePeriod timePeriod;
}

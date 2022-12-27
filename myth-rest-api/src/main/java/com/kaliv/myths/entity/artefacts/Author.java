package com.kaliv.myths.entity.artefacts;

import javax.persistence.*;

import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Nationality;
import com.kaliv.myths.entity.TimePeriod;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "authors")
public class Author extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "time_period_id", referencedColumnName = "id")
    private TimePeriod timePeriod;

    @ManyToOne
    @JoinColumn(name = "nationality_id", referencedColumnName = "id")
    private Nationality nationality;
}

package com.kaliv.myths.entity.artefacts;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "statue_images")
public class StatueImage extends Image{
    @ManyToOne
    @JoinColumn(name = "statue_id", referencedColumnName = "id")
    private Statue statue;
}

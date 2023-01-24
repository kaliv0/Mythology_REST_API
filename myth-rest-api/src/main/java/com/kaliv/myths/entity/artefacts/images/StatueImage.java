package com.kaliv.myths.entity.artefacts.images;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.kaliv.myths.entity.artefacts.Statue;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "statue_images")
public class StatueImage extends ArtImage {
    @ManyToOne
    @JoinColumn(name = "statue_id", referencedColumnName = "id")
    private Statue statue;
}

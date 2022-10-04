package com.kaliv.myths.entities.artefacts;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "statues")
public class Statue extends VisualArtwork {
}

package com.kaliv.myths.model.artefacts;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "statues")
public class Statue extends VisualArtwork {
}

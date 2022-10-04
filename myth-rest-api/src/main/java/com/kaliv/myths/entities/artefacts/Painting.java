package com.kaliv.myths.entities.artefacts;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "paintings")
public class Painting extends VisualArtwork {
}

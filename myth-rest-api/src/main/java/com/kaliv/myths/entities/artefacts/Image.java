package com.kaliv.myths.entities.artefacts;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "images", uniqueConstraints = @UniqueConstraint(columnNames = "image_url"))
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "image_url", nullable = false, unique = true)
    private String imageUrl;
}

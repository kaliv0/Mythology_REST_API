package com.kaliv.myths.entity.artefacts;

import javax.persistence.MappedSuperclass;

import com.kaliv.myths.entity.BaseEntity;

@MappedSuperclass
public abstract class Image extends BaseEntity {
//    @ManyToOne
//    @JoinColumn(name = "statue_id", referencedColumnName = "id")
//    private Statue statue;

    private String url;//TODO: refactor to byte[]
}

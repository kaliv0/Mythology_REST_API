package com.kaliv.myths.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kaliv.myths.model.artefacts.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
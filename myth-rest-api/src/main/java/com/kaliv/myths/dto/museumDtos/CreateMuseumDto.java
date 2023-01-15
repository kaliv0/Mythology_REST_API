package com.kaliv.myths.dto.museumDtos;

import javax.validation.constraints.NotBlank;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMuseumDto {
    @NotBlank
    private String name;

    private Collection<Long> statueIds;

    private Collection<Long> paintingIds;
}

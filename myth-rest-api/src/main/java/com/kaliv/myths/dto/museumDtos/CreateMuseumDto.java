package com.kaliv.myths.dto.museumDtos;

import javax.validation.constraints.NotBlank;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMuseumDto {
    @NotBlank
    private String name;

    private Set<Long> statueIds;

    private Set<Long> paintingIds;
}

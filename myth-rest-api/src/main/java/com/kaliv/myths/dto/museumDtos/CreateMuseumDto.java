package com.kaliv.myths.dto.museumDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

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

    @Positive
    private Set<Long> statueIds;

    @Positive
    private Set<Long> paintingIds;
}

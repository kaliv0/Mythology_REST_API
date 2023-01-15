package com.kaliv.myths.dto.museumDtos;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateMuseumDto {
    private String name;
    private Collection<Long> statuesToAdd;
    private Collection<Long> statuesToRemove;
    private Collection<Long> paintingsToAdd;
    private Collection<Long> paintingsToRemove;
}

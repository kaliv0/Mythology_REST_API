package com.kaliv.myths.dto.statueDtos;

import javax.validation.constraints.NotBlank;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateStatueDto {
    @NotBlank
    private String name;
    private Long AuthorId;
    private Long MythId;
    private Long MuseumId;
    private Set<Long> mythCharacterIds;
    private Set<Long> statueImageIds;
}

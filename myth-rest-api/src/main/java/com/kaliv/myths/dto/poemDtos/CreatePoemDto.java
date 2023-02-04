package com.kaliv.myths.dto.poemDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePoemDto {
    @NotBlank
    private String name;

    @Positive
    private Long AuthorId;

    @Positive
    private Long MythId;

    private String fullTextUrl;

    private String excerpt;

    private Set<@Positive Long> mythCharacterIds;
}

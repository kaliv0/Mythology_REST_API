package com.kaliv.myths.dto.poemDtos;

import javax.validation.constraints.NotBlank;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreatePoemDto {
    @NotBlank
    private String name;
    private Long AuthorId;
    private Long MythId;
    private String fullTextUrl;
    private String excerpt;
    private Collection<Long> mythCharacterIds;
}

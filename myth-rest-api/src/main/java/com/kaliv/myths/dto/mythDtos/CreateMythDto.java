package com.kaliv.myths.dto.mythDtos;

import javax.validation.constraints.NotBlank;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMythDto {
    @NotBlank
    private String name;

    @NotBlank
    private String plot;

    private Long nationalityId;

    private Set<Long> mythCharacterIds;
}

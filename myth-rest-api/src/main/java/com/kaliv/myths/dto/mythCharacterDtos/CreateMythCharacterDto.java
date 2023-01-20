package com.kaliv.myths.dto.mythCharacterDtos;

import javax.validation.constraints.NotBlank;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMythCharacterDto {
    @NotBlank
    private String name;

    private Long categoryId;

    private Long fatherId;

    private Long motherId;

    private Set<Long> mythIds;
}

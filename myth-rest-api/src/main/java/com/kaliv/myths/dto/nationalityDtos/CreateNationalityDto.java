package com.kaliv.myths.dto.nationalityDtos;

import javax.validation.constraints.NotBlank;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNationalityDto {
    @NotBlank
    private String name;

    private Set<Long> mythIds;

    private Set<Long> authorIds;
}

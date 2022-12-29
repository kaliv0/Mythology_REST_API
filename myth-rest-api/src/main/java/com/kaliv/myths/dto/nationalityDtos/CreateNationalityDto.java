package com.kaliv.myths.dto.nationalityDtos;

import javax.validation.constraints.NotBlank;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateNationalityDto {
    @NotBlank
    private String name;

    private Collection<Long> mythIds;

    private Collection<Long> authorIds;
}

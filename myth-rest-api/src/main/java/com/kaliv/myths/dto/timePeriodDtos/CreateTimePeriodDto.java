package com.kaliv.myths.dto.timePeriodDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTimePeriodDto {
    @NotBlank
    private String name;

    @NotBlank
    private String years;

    private Set<@Positive Long> authorIds;
}

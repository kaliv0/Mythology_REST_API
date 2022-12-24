package com.kaliv.myths.dto.timePeriodDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CreateUpdateTimePeriodDto {
    @NotBlank
    private String name;

    @NotBlank
    //TODO: add validation for years
    private String years;

    //TODO: add Author
}

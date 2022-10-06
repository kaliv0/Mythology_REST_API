package com.kaliv.myths.dtos.timePeriodDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CreateTimePeriodDto {
    @NotBlank
    private String name;

    @NotBlank
    //TODO: add validation for years
    private String years;

    //TODO: add Author
}

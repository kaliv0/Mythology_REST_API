package com.kaliv.myths.dto.timePeriodDtos;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateTimePeriodDto {
//    @NotBlank
    private String name;

//    @NotBlank
    //TODO: add validation for years
    private String years;

    private Collection<Long> authorIds;
}

package com.kaliv.myths.dto.authorDtos;

import javax.validation.constraints.NegativeOrZero;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUpdateAuthorDto {
    @NotEmpty
    private String name;

    private long timePeriodId;

    @Positive
    private long nationalityId;
}

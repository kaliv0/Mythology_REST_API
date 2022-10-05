package com.kaliv.myths.dtos.mythDtos;

import com.kaliv.myths.constants.ValidationMessages;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateMythDto {
    @NotEmpty
    @Size(min = 2, message = ValidationMessages.INVALID_MYTH_TITLE)
    private String title;

    @NotEmpty
    @Size(min = 30, message = ValidationMessages.INVALID_MYTH_PLOT)
    private String plot;

    //TODO: add nationality and list of characters
}

package com.kaliv.myths.dto.mythDtos;

import com.kaliv.myths.constant.messages.ValidationMessages;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class CreateUpdateMythDto {
    @NotBlank
    @Size(min = 2, message = ValidationMessages.INVALID_MYTH_TITLE)
    private String name;

    @NotBlank
    @Size(min = 30, message = ValidationMessages.INVALID_MYTH_PLOT)
    private String plot;

    //TODO: add nationality and list of characters

}

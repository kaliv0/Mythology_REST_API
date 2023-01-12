package com.kaliv.myths.dto.mythDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.Collection;

import com.kaliv.myths.constant.messages.ValidationMessages;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMythDto {
    @NotBlank
    @Size(min = 2, message = ValidationMessages.INVALID_MYTH_TITLE)
    private String name;

    @NotBlank
    @Size(min = 30, message = ValidationMessages.INVALID_MYTH_PLOT)
    private String plot;

    private Long nationalityId;

    private Collection<Long> mythCharacterIds;
}

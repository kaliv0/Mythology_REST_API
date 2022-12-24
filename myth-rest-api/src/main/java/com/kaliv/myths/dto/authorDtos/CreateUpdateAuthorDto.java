package com.kaliv.myths.dto.authorDtos;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateUpdateAuthorDto {
    @NotBlank
    private String name;
    private long authorId;
}

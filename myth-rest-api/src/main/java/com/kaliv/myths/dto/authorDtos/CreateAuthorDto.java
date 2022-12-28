package com.kaliv.myths.dto.authorDtos;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateAuthorDto extends UpdateAuthorDto {
    @NotEmpty
    private String name;
}

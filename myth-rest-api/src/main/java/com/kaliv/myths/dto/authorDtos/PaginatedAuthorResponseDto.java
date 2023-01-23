package com.kaliv.myths.dto.authorDtos;

import java.util.List;

import com.kaliv.myths.dto.PaginatedResponseDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedAuthorResponseDto extends PaginatedResponseDto {
    private List<AuthorResponseDto> content;
}

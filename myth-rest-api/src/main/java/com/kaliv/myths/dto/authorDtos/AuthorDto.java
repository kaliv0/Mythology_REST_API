package com.kaliv.myths.dto.authorDtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorDto {
    private long id;
    private String name;
    private long authorId;
}

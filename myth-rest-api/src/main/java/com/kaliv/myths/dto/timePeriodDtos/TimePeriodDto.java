package com.kaliv.myths.dto.timePeriodDtos;

import com.kaliv.myths.model.artefacts.Author;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TimePeriodDto {
    private long id;
    private String name;
    private String years;
    private Author author;
}

package com.kaliv.myths.dtos.timePeriodDtos;

import com.kaliv.myths.entities.artefacts.Author;
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

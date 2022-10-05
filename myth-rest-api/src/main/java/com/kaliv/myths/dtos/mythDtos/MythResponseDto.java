package com.kaliv.myths.dtos.mythDtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class MythResponseDto {
    private List<MythDto> content;
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean last;
}

package com.kaliv.myths.dto.mythDtos;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PaginatedMythResponseDto { //TODO:rename class
    private List<MythResponseDto> content;
    private int pageNumber;
    private int pageSize;
    private Long totalElements;
    private int totalPages;
    private boolean last;
}

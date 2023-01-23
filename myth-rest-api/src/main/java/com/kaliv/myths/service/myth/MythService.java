package com.kaliv.myths.service.myth;

import com.kaliv.myths.dto.mythDtos.*;

public interface MythService {
    PaginatedMythResponseDto getAllMyths(int pageNumber,
                                         int pageSize,
                                         String sortBy,
                                         String sortOrder
    );

    MythResponseDto getMythById(long id);

    MythDto createMyth(CreateMythDto dto);

    MythDto updateMyth(long id, UpdateMythDto dto);

    void deleteMyth(long id);
}

package com.kaliv.myths.service.myth;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.dto.mythDtos.*;

public interface MythService {
    PaginatedMythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria);

    MythResponseDto getMythById(long id);

    MythDto createMyth(CreateMythDto dto);

    MythDto updateMyth(long id, UpdateMythDto dto);

    void deleteMyth(long id);
}

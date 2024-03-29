package com.kaliv.myths.service.mythCharacter;

import com.kaliv.myths.dto.mythCharacterDtos.*;

public interface MythCharacterService {
    PaginatedMythCharacterResponseDto getAllMythCharacters(String fatherName,
                                                           String motherName,
                                                           String categoryName,
                                                           String mythName,
                                                           int pageNumber,
                                                           int pageSize,
                                                           String sortBy,
                                                           String sortOrder);

    MythCharacterResponseDto getMythCharacterById(long id);

    MythCharacterDto createMythCharacter(CreateMythCharacterDto dto);

    MythCharacterDto updateMythCharacter(long id, UpdateMythCharacterDto dto);

    void deleteMythCharacter(long id);
}

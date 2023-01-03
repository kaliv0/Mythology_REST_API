package com.kaliv.myths.service.mythCharacter;

import java.util.List;

import com.kaliv.myths.dto.mythCharacterDtos.CreateMythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterResponseDto;
import com.kaliv.myths.dto.mythCharacterDtos.UpdateMythCharacterDto;

public interface MythCharacterService {
    List<MythCharacterResponseDto> getAllMythCharacters();

    MythCharacterResponseDto getMythCharacterById(long id);

    MythCharacterDto createMythCharacter(CreateMythCharacterDto dto);

    MythCharacterDto updateMythCharacter(long id, UpdateMythCharacterDto dto);

    void deleteMythCharacter(long id);
}

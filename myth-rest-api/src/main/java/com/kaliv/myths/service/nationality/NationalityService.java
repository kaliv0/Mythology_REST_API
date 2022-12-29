package com.kaliv.myths.service.nationality;

import java.util.List;

import com.kaliv.myths.dto.nationalityDtos.CreateNationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityDto;
import com.kaliv.myths.dto.nationalityDtos.NationalityResponseDto;
import com.kaliv.myths.dto.nationalityDtos.UpdateNationalityDto;

public interface NationalityService {
    List<NationalityResponseDto> getAllNationalities();

    NationalityResponseDto getNationalityById(long id);

    NationalityDto createNationality(CreateNationalityDto dto);

    NationalityDto updateNationality(long id, UpdateNationalityDto dto);

    void deleteNationality(long id);
}

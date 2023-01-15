package com.kaliv.myths.service.museum;

import java.util.List;

import com.kaliv.myths.dto.museumDtos.CreateMuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumDto;
import com.kaliv.myths.dto.museumDtos.MuseumResponseDto;
import com.kaliv.myths.dto.museumDtos.UpdateMuseumDto;

public interface MuseumService {
    List<MuseumResponseDto> getAllMuseums();

    MuseumResponseDto getMuseumById(long id);

    MuseumDto createMuseum(CreateMuseumDto dto);

    MuseumDto updateMuseum(long id, UpdateMuseumDto dto);

    void deleteMuseum(long id);
}

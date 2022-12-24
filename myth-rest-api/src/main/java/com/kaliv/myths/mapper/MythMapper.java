package com.kaliv.myths.mapper;

import org.modelmapper.ModelMapper;

import com.kaliv.myths.model.Myth;
import com.kaliv.myths.dto.mythDtos.CreateUpdateMythDto;
import com.kaliv.myths.dto.mythDtos.MythDto;


public class MythMapper {

    final private static ModelMapper mapper = new ModelMapper();

    public static MythDto mythToDto(Myth myth) {
        return mapper.map(myth, MythDto.class);
    }

    public static Myth dtoToMyth(CreateUpdateMythDto mythDto) {
        return mapper.map(mythDto, Myth.class);
    }
}

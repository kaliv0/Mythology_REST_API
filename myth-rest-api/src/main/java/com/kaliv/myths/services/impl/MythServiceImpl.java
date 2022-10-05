package com.kaliv.myths.services.impl;

import com.kaliv.myths.dtos.mythsDtos.CreateMythDto;
import com.kaliv.myths.dtos.mythsDtos.MythDto;
import com.kaliv.myths.entities.Myth;
import com.kaliv.myths.exceptions.ResourceNotFoundException;
import com.kaliv.myths.mappers.MythMapper;
import com.kaliv.myths.repositories.MythRepository;
import com.kaliv.myths.services.contracts.MythService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MythServiceImpl implements MythService {

    private final MythRepository mythRepository;

    public MythServiceImpl(MythRepository mythRepository) {
        this.mythRepository = mythRepository;
    }

    @Override
    public List<MythDto> getAllMyths() {
        return mythRepository
                .findAll()
                .stream()
                .map(MythMapper.INSTANCE::entityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MythDto getMythById(long id) {
        Myth myth = mythRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));
        return MythMapper.INSTANCE.entityToDto(myth);
    }

    @Override
    public MythDto createMyth(CreateMythDto dto) {
        return null;
    }

    @Override
    public void deleteMyth(long id) {
        Myth myth = mythRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));
        mythRepository.delete(myth);
    }
}

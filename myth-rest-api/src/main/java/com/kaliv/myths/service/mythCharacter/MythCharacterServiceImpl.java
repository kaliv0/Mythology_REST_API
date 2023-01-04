package com.kaliv.myths.service.mythCharacter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.mythCharacterDtos.CreateMythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterResponseDto;
import com.kaliv.myths.dto.mythCharacterDtos.UpdateMythCharacterDto;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MythCharacterMapper;
import com.kaliv.myths.persistence.MythCharacterRepository;
import com.kaliv.myths.persistence.NationalityRepository;
import com.kaliv.myths.persistence.TimePeriodRepository;

@Service
public class MythCharacterServiceImpl implements MythCharacterService {

    private final MythCharacterRepository mythCharacterRepository;
    private final TimePeriodRepository timePeriodRepository;
    private final NationalityRepository nationalityRepository;
    private final MythCharacterMapper mapper;

    public MythCharacterServiceImpl(MythCharacterRepository mythCharacterRepository,
                                    TimePeriodRepository timePeriodRepository,
                                    NationalityRepository nationalityRepository,
                                    MythCharacterMapper mapper) {
        this.mythCharacterRepository = mythCharacterRepository;
        this.timePeriodRepository = timePeriodRepository;
        this.nationalityRepository = nationalityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<MythCharacterResponseDto> getAllMythCharacters() {
        return mythCharacterRepository.findAll()
                .stream().map(mapper::mythCharacterToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public MythCharacterResponseDto getMythCharacterById(long id) {
        MythCharacter mythCharacterInDb = mythCharacterRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, id));
        return mapper.mythCharacterToResponseDto(mythCharacterInDb);
    }

    @Override
    public MythCharacterDto createMythCharacter(CreateMythCharacterDto dto) {
//        String name = dto.getName();
//        if (mythCharacterRepository.existsByName(name)) {
//            throw new ResourceWithGivenValuesExistsException("MythCharacter", "name", name);
//        }
//
//        Long timePeriodId = dto.getTimePeriodId();
//        if (timePeriodId != null && !timePeriodRepository.existsById(timePeriodId)) {
//            throw new ResourceWithGivenValuesNotFoundException("Time period", "id", timePeriodId);
//        }
//
//        Long nationalityId = dto.getNationalityId();
//        if (nationalityId != null && !nationalityRepository.existsById(nationalityId)) {
//            throw new ResourceWithGivenValuesNotFoundException("Nationality", "id", nationalityId);
//        }
//
//        MythCharacter mythCharacter = mapper.dtoToEntity(dto, MythCharacter.class);
//        MythCharacter savedMythCharacter = mythCharacterRepository.save(mythCharacter);
//        return mapper.entityToDto(savedMythCharacter, MythCharacterDto.class);
        return null;
    }

    @Override
    public MythCharacterDto updateMythCharacter(long id, UpdateMythCharacterDto dto) {
//        MythCharacter mythCharacterInDb = mythCharacterRepository.findById(id)
//                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("MythCharacter", "id", id));
//
//        if (Optional.ofNullable(dto.getName()).isPresent()) {
//            String newName = dto.getName();
//            if (!newName.equals(mythCharacterInDb.getName()) && mythCharacterRepository.existsByName(newName)) {
//                throw new ResourceWithGivenValuesExistsException("MythCharacter", "name", newName);
//            }
//            mythCharacterInDb.setName(dto.getName());
//        }
//
//        if (Optional.ofNullable(dto.getTimePeriodId()).isPresent()) {
//            long timePeriodId = dto.getTimePeriodId();
//            TimePeriod timePeriodInDb = timePeriodRepository.findById(timePeriodId)
//                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("Time period", "id", timePeriodId));
//            mythCharacterInDb.setTimePeriod(timePeriodInDb);
//        }
//
//        if (Optional.ofNullable(dto.getNationalityId()).isPresent()) {
//            long nationalityId = dto.getNationalityId();
//            Nationality nationalityInDb = nationalityRepository.findById(nationalityId)
//                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("Nationality", "id", nationalityId));
//            mythCharacterInDb.setNationality(nationalityInDb);
//        }
//
//        MythCharacter savedMythCharacter = mythCharacterRepository.save(mythCharacterInDb);
//        return mapper.entityToDto(savedMythCharacter, MythCharacterDto.class);
        return null;

    }

    @Override
    public void deleteMythCharacter(long id) {
        MythCharacter mythCharacterInDb = mythCharacterRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.CHARACTERS, Fields.ID, id));
        mythCharacterRepository.delete(mythCharacterInDb);
    }
}

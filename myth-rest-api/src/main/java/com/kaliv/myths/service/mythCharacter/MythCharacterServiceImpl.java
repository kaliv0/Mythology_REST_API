package com.kaliv.myths.service.mythCharacter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.mythCharacterDtos.CreateMythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterDto;
import com.kaliv.myths.dto.mythCharacterDtos.MythCharacterResponseDto;
import com.kaliv.myths.dto.mythCharacterDtos.UpdateMythCharacterDto;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MythCharacterMapper;
import com.kaliv.myths.persistence.CategoryRepository;
import com.kaliv.myths.persistence.MythCharacterRepository;
import com.kaliv.myths.persistence.MythRepository;

@Service
public class MythCharacterServiceImpl implements MythCharacterService {

    private final MythCharacterRepository mythCharacterRepository;
    private final CategoryRepository categoryRepository;
    private final MythRepository mythRepository;
    private final MythCharacterMapper mapper;

    public MythCharacterServiceImpl(MythCharacterRepository mythCharacterRepository,
                                    CategoryRepository categoryRepository,
                                    MythRepository mythRepository,
                                    MythCharacterMapper mapper) {
        this.mythCharacterRepository = mythCharacterRepository;
        this.categoryRepository = categoryRepository;
        this.mythRepository = mythRepository;
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
        String name = dto.getName();
        if (mythCharacterRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.CHARACTER, Fields.NAME, name);
        }

        Long categoryId = dto.getCategoryId();
        if (categoryId != null && !categoryRepository.existsById(categoryId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.CATEGORY, Fields.ID, categoryId);
        }

        List<Long> mythIds = new ArrayList<>(dto.getMythIds());
        List<Myth> myths = mythRepository.findAllById(mythIds);
        if (myths.size() != mythIds.size()) {
            throw new ResourceListNotFoundException(Sources.MYTHS, Fields.IDS);
        }

        MythCharacter mythCharacter = mapper.dtoToMythCharacter(dto);

        Long fatherId = dto.getFatherId();
        if (fatherId != null) {
            mythCharacterRepository.findById(fatherId)
                    .ifPresentOrElse(mythCharacter::setFather, () -> {
                        throw new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, fatherId);
                    });
        }
        Long motherId = dto.getMotherId();
        if (motherId != null) {
            mythCharacterRepository.findById(motherId)
                    .ifPresentOrElse(mythCharacter::setMother, () -> {
                        throw new ResourceWithGivenValuesNotFoundException(Sources.CHARACTER, Fields.ID, motherId);
                    });
        }

        MythCharacter savedMythCharacter = mythCharacterRepository.save(mythCharacter);

        myths.forEach(m -> m.getMythCharacters().add(savedMythCharacter));
        mythRepository.saveAll(myths);

        return mapper.mythCharacterToDto(savedMythCharacter);
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
//        if (Optional.ofNullable(dto.getCategoryId()).isPresent()) {
//            long categoryId = dto.getCategoryId();
//            Category categoryInDb = categoryRepository.findById(categoryId)
//                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("Time period", "id", categoryId));
//            mythCharacterInDb.setCategory(categoryInDb);
//        }
//
//        if (Optional.ofNullable(dto.getMythId()).isPresent()) {
//            long mythId = dto.getMythId();
//            Myth mythInDb = mythRepository.findById(mythId)
//                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("Myth", "id", mythId));
//            mythCharacterInDb.setMyth(mythInDb);
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

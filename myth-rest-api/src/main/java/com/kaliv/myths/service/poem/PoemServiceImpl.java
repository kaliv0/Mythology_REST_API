package com.kaliv.myths.service.poem;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.kaliv.myths.common.utils.Tuple;
import com.kaliv.myths.constant.params.Fields;
import com.kaliv.myths.constant.params.Sources;
import com.kaliv.myths.dto.poemDtos.CreatePoemDto;
import com.kaliv.myths.dto.poemDtos.PoemDto;
import com.kaliv.myths.dto.poemDtos.PoemResponseDto;
import com.kaliv.myths.dto.poemDtos.UpdatePoemDto;
import com.kaliv.myths.entity.BaseEntity;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.entity.MythCharacter;
import com.kaliv.myths.entity.artefacts.Author;
import com.kaliv.myths.entity.artefacts.Poem;
import com.kaliv.myths.exception.alreadyExists.ResourceAlreadyExistsException;
import com.kaliv.myths.exception.alreadyExists.ResourceWithGivenValuesExistsException;
import com.kaliv.myths.exception.invalidInput.DuplicateEntriesException;
import com.kaliv.myths.exception.notFound.ResourceListNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceNotFoundException;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.PoemMapper;
import com.kaliv.myths.persistence.AuthorRepository;
import com.kaliv.myths.persistence.MythCharacterRepository;
import com.kaliv.myths.persistence.MythRepository;
import com.kaliv.myths.persistence.PoemRepository;

@Service
public class PoemServiceImpl implements PoemService {

    private final PoemRepository poemRepository;
    private final AuthorRepository authorRepository;
    private final MythRepository mythRepository;
    private final MythCharacterRepository mythCharacterRepository;
    private final PoemMapper mapper;

    public PoemServiceImpl(PoemRepository poemRepository,
                           AuthorRepository authorRepository,
                           MythRepository mythRepository,
                           MythCharacterRepository mythCharacterRepository,
                           PoemMapper mapper) {
        this.poemRepository = poemRepository;
        this.authorRepository = authorRepository;
        this.mythRepository = mythRepository;
        this.mythCharacterRepository = mythCharacterRepository;
        this.mapper = mapper;
    }

    @Override
    public List<PoemResponseDto> getAllPoems() {
        return poemRepository.findAll()
                .stream().map(mapper::poemToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public PoemResponseDto getPoemById(long id) {
        Poem poemInDb = poemRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.POEM, Fields.ID, id));
        return mapper.poemToResponseDto(poemInDb);
    }

    @Override
    public PoemDto createPoem(CreatePoemDto dto) {
        String name = dto.getName();
        if (poemRepository.existsByName(name)) {
            throw new ResourceWithGivenValuesExistsException(Sources.POEM, Fields.NAME, name);
        }

        Long authorId = dto.getAuthorId();
        if (authorId != null && !authorRepository.existsById(authorId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId);
        }

        Long mythId = dto.getMythId();
        if (mythId != null && !mythRepository.existsById(mythId)) {
            throw new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId);
        }

        List<Long> mythCharacterIds = new ArrayList<>(dto.getMythCharacterIds());
        List<MythCharacter> mythCharacters = mythCharacterRepository.findAllById(mythCharacterIds);
        if (mythCharacters.size() != mythCharacterIds.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }

        Poem poem = mapper.dtoToPoem(dto);
        Poem savedPoem = poemRepository.save(poem);
        return mapper.poemToDto(savedPoem);
    }

    @Override
    public PoemDto updatePoem(long id, UpdatePoemDto dto) {
        Poem poemInDb = poemRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.POEM, Fields.ID, id));

        if (Optional.ofNullable(dto.getName()).isPresent()) {
            String newName = dto.getName();
            if (!newName.equals(poemInDb.getName()) && poemRepository.existsByName(newName)) {
                throw new ResourceWithGivenValuesExistsException(Sources.POEM, Fields.NAME, newName);
            }
            poemInDb.setName(dto.getName());
        }

        if (Optional.ofNullable(dto.getAuthorId()).isPresent()) {
            long authorId = dto.getAuthorId();
            Author authorInDb = authorRepository.findById(authorId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.AUTHOR, Fields.ID, authorId));
            poemInDb.setAuthor(authorInDb);
        }

        if (Optional.ofNullable(dto.getMythId()).isPresent()) {
            long mythId = dto.getMythId();
            Myth mythInDb = mythRepository.findById(mythId)
                    .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.MYTH, Fields.ID, mythId));
            poemInDb.setMyth(mythInDb);
        }

        Optional.ofNullable(dto.getFullTextUrl()).ifPresent(poemInDb::setFullTextUrl);
        Optional.ofNullable(dto.getExcerpt()).ifPresent(poemInDb::setExcerpt);

        Tuple<List<MythCharacter>, List<MythCharacter>> mythCharactersToUpdate = this.getValidMythCharacters(dto, poemInDb);
        List<MythCharacter> mythCharactersToAdd = mythCharactersToUpdate.getFirst();
        List<MythCharacter> mythCharactersToRemove = mythCharactersToUpdate.getSecond();
        poemInDb.getMythCharacters().addAll(new HashSet<>(mythCharactersToAdd));
        poemInDb.getMythCharacters().removeAll(new HashSet<>(mythCharactersToRemove));
        poemRepository.save(poemInDb);

        return mapper.poemToDto(poemInDb);
    }

    @Override
    public void deletePoem(long id) {
        Poem poemInDb = poemRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException(Sources.POEM, Fields.ID, id));
        poemRepository.delete(poemInDb);
    }

    private Tuple<List<MythCharacter>, List<MythCharacter>> getValidMythCharacters(UpdatePoemDto dto, Poem poemInDb) {
        List<Long> mythCharactersToAddIds = new ArrayList<>(dto.getMythCharactersToAdd());
        List<Long> mythCharactersToRemoveIds = new ArrayList<>(dto.getMythCharactersToRemove());
        //check if user tries to add and remove same myth character
        if (!Collections.disjoint(mythCharactersToAddIds, mythCharactersToRemoveIds)) {
            throw new DuplicateEntriesException(Sources.ADD_CHARACTERS, Sources.REMOVE_CHARACTERS);
        }
        //check if user tries to add myth character that is already in the list
        if (poemInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .anyMatch(mythCharactersToAddIds::contains)) {
            throw new ResourceAlreadyExistsException(Sources.CHARACTER);
        }
        //check if user tries to remove myth character that is not in the list
        if (!poemInDb.getMythCharacters().stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet())
                .containsAll(mythCharactersToRemoveIds)) {
            throw new ResourceNotFoundException(Sources.CHARACTER);
        }

        List<MythCharacter> mythCharactersToAdd = mythCharacterRepository.findAllById(mythCharactersToAddIds);
        List<MythCharacter> mythCharactersToRemove = mythCharacterRepository.findAllById(mythCharactersToRemoveIds);
        if (mythCharactersToAddIds.size() != mythCharactersToAdd.size()
                || mythCharactersToRemoveIds.size() != mythCharactersToRemove.size()) {
            throw new ResourceListNotFoundException(Sources.CHARACTERS, Fields.IDS);
        }
        return new Tuple<>(mythCharactersToAdd, mythCharactersToRemove);
    }
}

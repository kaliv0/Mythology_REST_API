package com.kaliv.myths.service.myth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.PaginationCriteria;
import com.kaliv.myths.common.SortCriteria;
import com.kaliv.myths.dto.mythDtos.CreateUpdateMythDto;
import com.kaliv.myths.dto.mythDtos.MythDto;
import com.kaliv.myths.dto.mythDtos.MythResponseDto;
import com.kaliv.myths.exception.MythAPIException;
import com.kaliv.myths.exception.ResourceNotFoundException;
import com.kaliv.myths.mapper.GenericMapper;
import com.kaliv.myths.model.Myth;
import com.kaliv.myths.persistence.MythRepository;

@Service
public class MythServiceImpl implements MythService {

    private final MythRepository mythRepository;
    private final GenericMapper mapper;

    public MythServiceImpl(MythRepository mythRepository, GenericMapper mapper) {
        this.mythRepository = mythRepository;
        this.mapper = mapper;
    }

    @Override
    public MythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria) {
        int page = paginationCriteria.getPage();
        int size = paginationCriteria.getSize();
        String sortDir = sortCriteria.getSortOrder();
        String sortAttr = sortCriteria.getSortAttribute();

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortAttr);
        Page<Myth> myths = mythRepository.findAll(pageable);
        List<Myth> listOfMyths = myths.getContent(); //TODO:check if redundant

        List<MythDto> content = listOfMyths.stream()
                .map(myth -> mapper.entityToDto(myth, MythDto.class))
                .collect(Collectors.toList());

        MythResponseDto mythResponseDto = new MythResponseDto();
        mythResponseDto.setContent(content);
        mythResponseDto.setPageNumber(myths.getNumber());
        mythResponseDto.setPageSize(myths.getSize());
        mythResponseDto.setTotalElements(myths.getTotalElements());
        mythResponseDto.setTotalPages(myths.getTotalPages());
        mythResponseDto.setLast(myths.isLast());

        return mythResponseDto;
    }

    @Override
    public MythDto getMythById(long id) {
        Myth mythInDb = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));
        return mapper.entityToDto(mythInDb, MythDto.class);
    }

    @Override
    public MythDto createMyth(CreateUpdateMythDto dto) {
        String title = dto.getTitle();
        if (mythRepository.findByTitle(title).isPresent()) {
            throw new MythAPIException("Myth", "title", title);
        }

        //TODO: how to fill in the list of characters?

        Myth myth = mapper.dtoToEntity(dto, Myth.class);
        mythRepository.save(myth);
        return mapper.entityToDto(myth, MythDto.class);
    }

    @Override
    public MythDto updateMyth(long id, CreateUpdateMythDto dto) {
        Myth myth = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));

//        check if myth with the same name already exists
        String newTitle = dto.getTitle();
        if (mythRepository.findByTitle(newTitle).isPresent()) {
            throw new MythAPIException("Myth", "title", newTitle);
        }

        BeanUtils.copyProperties(dto, myth);
        mythRepository.save(myth);
        return mapper.entityToDto(myth, MythDto.class);
    }

    @Override
    public void deleteMyth(long id) {
        Myth myth = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));
        mythRepository.delete(myth);
    }
}

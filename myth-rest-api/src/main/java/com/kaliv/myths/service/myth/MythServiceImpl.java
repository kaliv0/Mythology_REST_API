package com.kaliv.myths.service.myth;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kaliv.myths.common.criteria.PaginationCriteria;
import com.kaliv.myths.common.criteria.SortCriteria;
import com.kaliv.myths.dto.mythDtos.CreateMythDto;
import com.kaliv.myths.dto.mythDtos.MythDto;
import com.kaliv.myths.dto.mythDtos.MythResponseDto;
import com.kaliv.myths.dto.mythDtos.PaginatedMythResponseDto;
import com.kaliv.myths.entity.Myth;
import com.kaliv.myths.exception.notFound.ResourceWithGivenValuesNotFoundException;
import com.kaliv.myths.mapper.MythMapper;
import com.kaliv.myths.persistence.MythRepository;

@Service
public class MythServiceImpl implements MythService {

    private final MythRepository mythRepository;
    private final MythMapper mapper;

    public MythServiceImpl(MythRepository mythRepository, MythMapper mapper) {
        this.mythRepository = mythRepository;
        this.mapper = mapper;
    }

    @Override
    public PaginatedMythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria) {
        int page = paginationCriteria.getPage();
        int size = paginationCriteria.getSize();
        String sortDir = sortCriteria.getSortOrder();
        String sortAttr = sortCriteria.getSortAttribute();

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortAttr);
        Page<Myth> myths = mythRepository.findAll(pageable);
        List<Myth> listOfMyths = myths.getContent(); //TODO:check if redundant

        List<MythResponseDto> content = listOfMyths.stream()
                .map(mapper::mythToResponseDto)
                .collect(Collectors.toList());

        PaginatedMythResponseDto mythResponseDto = new PaginatedMythResponseDto();
        mythResponseDto.setContent(content);
        mythResponseDto.setPageNumber(myths.getNumber());
        mythResponseDto.setPageSize(myths.getSize());
        mythResponseDto.setTotalElements(myths.getTotalElements());
        mythResponseDto.setTotalPages(myths.getTotalPages());
        mythResponseDto.setLast(myths.isLast());
        return mythResponseDto;
    }

    @Override
    public MythResponseDto getMythById(long id) {
        Myth mythInDb = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("Myth", "id", id));
        return mapper.mythToResponseDto(mythInDb);
    }

    @Override
    public MythDto createMyth(CreateMythDto dto) {
        return null;
    }

//    @Override
//    public MythDto updateMyth(long id, UpdateMythDto dto) {
//        Myth myth = mythRepository.findById(id)
//                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("Myth", "id", id));
//
////        check if myth with the same name already exists=> TODO: unnecessary?
//        String newName = dto.getName();
//        if (mythRepository.existsByName(newName)) {
//            throw new ResourceWithGivenValuesExistsException("Myth", "title", newName);
//        }
//
//        //TODO: add ignoreProperties
//        BeanUtils.copyProperties(dto, myth);
//        mythRepository.save(myth);
//        return mapper.entityToDto(myth, MythDto.class);
//    }

    @Override
    public void deleteMyth(long id) {
        Myth myth = mythRepository.findById(id)
                .orElseThrow(() -> new ResourceWithGivenValuesNotFoundException("Myth", "id", id));
        mythRepository.delete(myth);
    }
}

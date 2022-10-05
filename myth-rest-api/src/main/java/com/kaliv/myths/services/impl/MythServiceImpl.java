package com.kaliv.myths.services.impl;

import com.kaliv.myths.common.PaginationCriteria;
import com.kaliv.myths.common.SortCriteria;
import com.kaliv.myths.dtos.mythDtos.CreateMythDto;
import com.kaliv.myths.dtos.mythDtos.MythDto;
import com.kaliv.myths.dtos.mythDtos.MythResponseDto;
import com.kaliv.myths.dtos.mythDtos.UpdateMythDto;
import com.kaliv.myths.entities.Myth;
import com.kaliv.myths.exceptions.ResourceNotFoundException;
import com.kaliv.myths.mappers.MythMapper;
import com.kaliv.myths.repositories.MythRepository;
import com.kaliv.myths.services.contracts.MythService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public MythResponseDto getAllMyths(PaginationCriteria paginationCriteria, SortCriteria sortCriteria) {
        int page = paginationCriteria.getPage();
        int size = paginationCriteria.getSize();
        String sortDir = String.valueOf(sortCriteria.getSortOrder());
        String sortAttr = sortCriteria.getSortAttribute();

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.fromString(sortDir), sortAttr);
        Page<Myth> myths = mythRepository.findAll(pageable);
        List<Myth> listOfMyths = myths.getContent(); //TODO:check if redundant

        List<MythDto> content = listOfMyths.stream()
                .map(MythMapper.INSTANCE::entityToDto).collect(Collectors.toList());

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
        Myth myth = mythRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));
        return MythMapper.INSTANCE.entityToDto(myth);
    }

    @Override
    public MythDto createMyth(CreateMythDto dto) {
        //TODO: check if a myth with the same name already exists

        Myth myth = new Myth();
        myth.setTitle(dto.getTitle());
        myth.setPlot(dto.getPlot());
        //TODO: how to fill in the list of characters?

        mythRepository.save(myth);
        return (MythMapper.INSTANCE.entityToDto(myth));
    }

    @Override
    public MythDto updateMyth(long id, UpdateMythDto dto) {
        Myth myth = mythRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));

        //check if myth with the same name already exists
//        if (myth.getTitle().equals(dto.getTitle())) {
//            throw new MythApiException("Title already exist!");
//        }

        BeanUtils.copyProperties(dto, myth);
        mythRepository.save(myth);
        return MythMapper.INSTANCE.entityToDto(myth);
    }

    @Override
    public void deleteMyth(long id) {
        Myth myth = mythRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Myth", "id", id));
        mythRepository.delete(myth);
    }
}

package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.messages.ResponseMessages;
import com.kaliv.myths.dto.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodResponseDto;
import com.kaliv.myths.dto.timePeriodDtos.UpdateTimePeriodDto;
import com.kaliv.myths.service.timePeriod.TimePeriodService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Time periods")
@RestController
@RequestMapping("/api/v1/time-periods")
public class TimePeriodController {

    private final TimePeriodService timePeriodService;

    @Autowired
    public TimePeriodController(TimePeriodService timePeriodService) {
        this.timePeriodService = timePeriodService;
    }

    @GetMapping
    public ResponseEntity<List<TimePeriodResponseDto>> getAllTimePeriods() {
        return ResponseEntity.ok(timePeriodService.getAllTimePeriods());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimePeriodResponseDto> getTimePeriodById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(timePeriodService.getTimePeriodById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('WRITE')")
    public ResponseEntity<TimePeriodDto> createTimePeriod(@Valid @RequestBody CreateTimePeriodDto dto) {
        return new ResponseEntity<>(timePeriodService.createTimePeriod(dto), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('UPDATE')")
    public TimePeriodDto updateTimePeriod(@PathVariable("id") long id, @Valid @RequestBody UpdateTimePeriodDto dto) {
        return timePeriodService.updateTimePeriod(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('DELETE')")
    public ResponseEntity<String> deleteTimePeriod(@PathVariable(name = "id") long id) {
        timePeriodService.deleteTimePeriod(id);
        return new ResponseEntity<>(ResponseMessages.TIME_PERIOD_DELETED, HttpStatus.OK);
    }
}
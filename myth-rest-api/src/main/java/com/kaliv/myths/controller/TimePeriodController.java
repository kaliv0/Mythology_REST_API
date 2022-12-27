package com.kaliv.myths.controller;

import javax.validation.Valid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kaliv.myths.constant.ResponseMessages;
import com.kaliv.myths.dto.timePeriodDtos.CreateUpdateTimePeriodDto;
import com.kaliv.myths.dto.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.service.timePeriod.TimePeriodService;

@RestController
@RequestMapping("/api/v1/time-periods")
public class TimePeriodController {

    private final TimePeriodService timePeriodService;

    public TimePeriodController(TimePeriodService timePeriodService) {
        this.timePeriodService = timePeriodService;
    }

    @GetMapping
    public ResponseEntity<List<TimePeriodDto>> getAllTimePeriods() {
        return ResponseEntity.ok(timePeriodService.getAllTimePeriods());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TimePeriodDto> getTimePeriodById(@PathVariable(name = "id") long id) {
        return ResponseEntity.ok(timePeriodService.getTimePeriodById(id));
    }

    @PostMapping
    public ResponseEntity<TimePeriodDto> createTimePeriod(@Valid @RequestBody CreateUpdateTimePeriodDto dto) {
        return new ResponseEntity<>(timePeriodService.createTimePeriod(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public TimePeriodDto updateTimePeriod(@PathVariable("id") long id, @Valid @RequestBody CreateUpdateTimePeriodDto dto) {
        return timePeriodService.updateTimePeriod(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimePeriod(@PathVariable(name = "id") long id) {
        timePeriodService.deleteTimePeriod(id);
        return new ResponseEntity<>(ResponseMessages.TIME_PERIOD_DELETED, HttpStatus.OK);
    }
}
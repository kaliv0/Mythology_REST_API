package com.kaliv.myths.controllers;

import com.kaliv.myths.constants.ResponseMessages;
import com.kaliv.myths.dtos.timePeriodDtos.CreateTimePeriodDto;
import com.kaliv.myths.dtos.timePeriodDtos.TimePeriodDto;
import com.kaliv.myths.dtos.timePeriodDtos.UpdateTimePeriodDto;
import com.kaliv.myths.services.contracts.TimePeriodService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<TimePeriodDto> createTimePeriod(@Valid @RequestBody CreateTimePeriodDto dto) {
        return new ResponseEntity<>(timePeriodService.createTimePeriod(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public TimePeriodDto updateTimePeriod(@PathVariable("id") Long id, @Valid @RequestBody UpdateTimePeriodDto dto) {
        return timePeriodService.updateTimePeriod(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTimePeriod(@PathVariable(name = "id") long id) {
        timePeriodService.deleteTimePeriod(id);
        return new ResponseEntity<>(ResponseMessages.TIME_PERIOD_DELETED, HttpStatus.OK);
    }

}
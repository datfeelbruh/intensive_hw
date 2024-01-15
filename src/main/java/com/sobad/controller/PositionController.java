package com.sobad.controller;

import com.sobad.dto.PositionDto;
import com.sobad.entity.Position;
import com.sobad.service.PositionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/position")
@RequiredArgsConstructor
public class PositionController {
    private final PositionService service;

    @PostMapping
    public ResponseEntity<Position> createEmployee(@RequestBody PositionDto positionDto) {
        return new ResponseEntity<>(
                service.create(positionDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Position> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                service.findById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<Position>> findAll() {
        return new ResponseEntity<>(
                service.findAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable("id") Long id, @RequestBody PositionDto positionDto) {
        service.update(id, positionDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        service.delete(id);
    }
}

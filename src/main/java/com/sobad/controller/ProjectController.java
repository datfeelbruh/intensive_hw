package com.sobad.controller;

import com.sobad.dto.ProjectDto;
import com.sobad.dto.ProjectReadDto;
import com.sobad.entity.Project;
import com.sobad.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService service;


    @PostMapping
    public ResponseEntity<ProjectReadDto> createEmployee(@RequestBody ProjectDto projectDto) {
        return new ResponseEntity<>(
                service.create(projectDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                service.findById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<Project>> findAll() {
        return new ResponseEntity<>(
                service.findAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable("id") Long id, @RequestBody ProjectDto projectDto) {
        service.update(id, projectDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        service.delete(id);
    }
}

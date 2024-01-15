package com.sobad.controller;

import com.sobad.dto.EmployeeDto;
import com.sobad.entity.Employee;
import com.sobad.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    @Autowired
    private EmployeeService service;


    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeDto employeeDto) {
        return new ResponseEntity<>(
                service.create(employeeDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public void findById(@PathVariable("id") Long id) {
        service.findById(id);
    }

    @GetMapping
    public ResponseEntity<List<Employee>> findAll() {
        return new ResponseEntity<>(
                service.findAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable("id") Long id, @RequestBody EmployeeDto employeeDto) {
        service.update(id, employeeDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        service.delete(id);
    }
}

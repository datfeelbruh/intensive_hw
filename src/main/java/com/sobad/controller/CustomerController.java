package com.sobad.controller;

import com.sobad.dto.CustomerDto;
import com.sobad.dto.CustomerReadDto;
import com.sobad.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService service;


    @PostMapping
    public ResponseEntity<CustomerReadDto> createEmployee(@RequestBody CustomerDto customerDto) {
        return new ResponseEntity<>(
                service.create(customerDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerReadDto> findById(@PathVariable("id") Long id) {
        return new ResponseEntity<>(
                service.findById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<CustomerReadDto>> findAll() {
        return new ResponseEntity<>(
                service.findAll(),
                HttpStatus.OK
        );
    }

    @PutMapping("/{id}")
    public void updateEmployee(@PathVariable("id") Long id, @RequestBody CustomerDto customerDto) {
        service.update(id, customerDto);
    }

    @DeleteMapping("/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
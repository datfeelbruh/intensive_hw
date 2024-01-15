package com.sobad.service;

import com.sobad.dao.CustomerDao;
import com.sobad.dto.CustomerDto;
import com.sobad.dto.CustomerReadDto;
import com.sobad.entity.Customer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerReadDto create(CustomerDto customerDto) {
        Customer save = customerDao.save(customerDto);
        return CustomerReadDto.builder()
                .customerName(save.getName())
                .build();
    }

    public CustomerReadDto findById(Long id) {
        Optional<Customer> byId = Optional.ofNullable(customerDao.findById(id));
        CustomerReadDto customerReadDto = CustomerReadDto.builder().build();
        if (byId.isPresent()) {
            customerReadDto = CustomerReadDto.builder()
                    .customerName(byId.get().getName())
                    .build();
        }
        return customerReadDto;
    }

    public List<CustomerReadDto> findAll() {
        return customerDao.findAll().stream()
                .map(e -> CustomerReadDto.builder().customerName(e.getName()).build())
                .toList();

    }

    public void update(Long id, CustomerDto customerDto) {
        customerDao.update(id, customerDto);
    }

    public void delete(Long id) {
        customerDao.delete(id);
    }
}

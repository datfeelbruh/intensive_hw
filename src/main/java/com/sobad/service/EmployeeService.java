package com.sobad.service;

import com.sobad.dao.EmployeeDao;
import com.sobad.dto.EmployeeDto;
import com.sobad.entity.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeDao employeeDao;

    public Employee create(EmployeeDto employeeDto) {
        return employeeDao.save(employeeDto);
    }

    public List<Employee> findAll() {
        return employeeDao.findAll();
    }

    public Employee findById(Long id) {
        return employeeDao.findById(id);
    }

    public void update(Long id, EmployeeDto employeeDto) {
        employeeDao.update(id, employeeDto);
    }

    public void delete(Long id) {
        employeeDao.delete(id);
    }
}

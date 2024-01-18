package com.sobad.service;

import com.sobad.dao.EmployeeDao;
import com.sobad.dto.EmployeeDto;
import com.sobad.entity.Employee;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class EmployeeService {
    private final EmployeeDao employeeDao = EmployeeDao.getInstance();

    private static final EmployeeService INSTANCE = new EmployeeService();

    private EmployeeService() {

    }

    public static EmployeeService getInstance() {
        return INSTANCE;
    }

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

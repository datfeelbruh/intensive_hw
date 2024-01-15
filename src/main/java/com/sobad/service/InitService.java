package com.sobad.service;

import com.sobad.dao.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InitService {
    private final CustomerDao customerDao;
    private final EmployeeDao employeeDao;
    private final ProjectDao projectDao;
    private final PositionDao positionDao;
    private final EmployeePositionDao employeePositionDao;

    public void init() {
        customerDao.init();
        positionDao.init();
        projectDao.init();
        employeeDao.init();
        employeePositionDao.init();
    }

    public void clear() {
        employeePositionDao.deleteAll();
        positionDao.deleteAll();
        employeeDao.deleteAll();
        projectDao.deleteAll();
        customerDao.deleteAll();
    }
}

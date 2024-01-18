package com.sobad.service;

import com.sobad.dao.*;

public class InitService {
    private final CustomerDao customerDao = CustomerDao.getInstance();
    private final EmployeeDao employeeDao = EmployeeDao.getInstance();
    private final ProjectDao projectDao = ProjectDao.getInstance();
    private final PositionDao positionDao = PositionDao.getInstance();
    private final EmployeePositionDao employeePositionDao = EmployeePositionDao.getInstance();

    private static final InitService INSTANCE = new InitService();

    private InitService() {

    }

    public static InitService getInstance() {
        return INSTANCE;
    }

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

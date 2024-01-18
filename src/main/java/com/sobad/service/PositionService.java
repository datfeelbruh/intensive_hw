package com.sobad.service;

import com.sobad.dao.PositionDao;
import com.sobad.dto.PositionDto;
import com.sobad.entity.Position;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class PositionService {
    private final PositionDao positionDao = PositionDao.getInstance();

    private static final PositionService INSTANCE = new PositionService();

    private PositionService() {

    }

    public static PositionService getInstance() {
        return INSTANCE;
    }

    public Position create(PositionDto positionDto) {
        return positionDao.save(positionDto);
    }

    public Position findById(Long id) {
        return positionDao.findById(id);
    }

    public List<Position> findAll() {
        return positionDao.findAll();
    }

    public void update(Long id, PositionDto positionDto) {
        positionDao.update(id, positionDto);
    }

    public void delete(Long id) {
        positionDao.delete(id);
    }
}

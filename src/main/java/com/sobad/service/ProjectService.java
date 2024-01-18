package com.sobad.service;

import com.sobad.dao.ProjectDao;
import com.sobad.dto.ProjectDto;
import com.sobad.dto.ProjectReadDto;
import com.sobad.entity.Project;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class ProjectService {
    private final ProjectDao projectDao = ProjectDao.getInstance();

    private static final ProjectService INSTANCE = new ProjectService();

    private ProjectService() {

    }

    public static ProjectService getInstance() {
        return INSTANCE;
    }


    public ProjectReadDto create(ProjectDto projectDto) {
        Project save = projectDao.save(projectDto);
        return ProjectReadDto.builder()
                .id(save.getProjectId())
                .name(save.getProjectName())
                .customerId(save.getCustomer().getId())
                .customerName(save.getCustomer().getName())
                .build();
    }

    public Project findById(Long id) {
        return projectDao.findById(id);
    }

    public List<Project> findAll() {
        return projectDao.findAll();
    }

    public void update(Long id, ProjectDto projectDto) {
        projectDao.update(id, projectDto);
    }

    public void delete(Long id) {
        projectDao.delete(id);
    }
}

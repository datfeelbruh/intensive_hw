package com.sobad.dao;

import com.sobad.dto.ProjectDto;
import com.sobad.entity.Customer;
import com.sobad.entity.Project;
import com.sobad.exception.DaoException;
import com.sobad.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProjectDao {

    private static final ProjectDao INSTANCE = new ProjectDao();

    private static final String INIT_SQL = """
            INSERT INTO project (project_name, customer_id) VALUES
            ('Bank', 1), ('Startup', 2), ('CoffeeShop', 3);
            """;

    private static final String SAVE_SQL = """
            INSERT INTO project (project_name, customer_id)
            VALUES (?, ?)
            """;

    private static final String DELETE_ALL_SQL = """
            TRUNCATE project RESTART IDENTITY CASCADE;
            """;
    private static final String FIND_ALL_SQL = """
            SELECT p.project_id, p.project_name, c.customer_id, c.customer_name
            FROM project p
            JOIN customer c ON c.customer_id = p.customer_id
            """;

    private static final String FIND_BY_ID_SQL = """
            WHERE project_id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE project
            SET project_name = ?, customer_id = ?
            WHERE customer_id = ?;
            """;
    private static final String DELETE_SQL = """
            DELETE FROM project
            WHERE project.project_id = ?
            """;

    private ProjectDao() {}

    public static ProjectDao getInstance() {
        return INSTANCE;
    }

    public void init() {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(INIT_SQL);
            prepareStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void deleteAll() {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(DELETE_ALL_SQL);
            prepareStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Project save(ProjectDto projectDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, projectDto.getProjectName());
            prepareStatement.setLong(2, projectDto.getCustomerId());

            prepareStatement.executeUpdate();
            var generatedKeys = prepareStatement.getGeneratedKeys();

            Project project = null;
            Customer customer = null;
            if (generatedKeys.next()) {
                var getCustomer = connection.prepareStatement(FIND_ALL_SQL + FIND_BY_ID_SQL);
                getCustomer.setLong(1, generatedKeys.getLong("project_id"));
                ResultSet resultSet = getCustomer.executeQuery();
                if (resultSet.next()) {
                    customer = Customer.builder()
                            .id(resultSet.getLong("customer_id"))
                            .name(resultSet.getString("customer_name"))
                            .build();
                }

                project = Project.builder()
                        .projectId(generatedKeys.getLong("project_id"))
                        .projectName(generatedKeys.getString("project_name"))
                        .customer(customer)
                        .build();
            }
            return project;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Project> findAll() {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL);
            var resultSet = prepareStatement.executeQuery();
            List<Project> projects = new ArrayList<>();
            while (resultSet.next()) {
                projects.add(Project.builder()
                        .projectId(resultSet.getLong("project_id"))
                        .projectName(resultSet.getString("project_name"))
                        .customer(Customer.builder()
                                .id(resultSet.getLong("customer_id"))
                                .name(resultSet.getString("customer_name"))
                                .build())
                        .build());
            }
            return projects;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public Project findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL + FIND_BY_ID_SQL);
            prepareStatement.setLong(1, id);

            var resultSet = prepareStatement.executeQuery();
            Project project = null;
            if (resultSet.next()) {
                project = Project.builder()
                        .projectId(resultSet.getLong("project_id"))
                        .projectName(resultSet.getString("project_name"))
                        .customer(Customer.builder()
                                .id(resultSet.getLong("customer_id"))
                                .name(resultSet.getString("customer_name"))
                                .build())
                        .build();
            }
            return project;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Long id, ProjectDto projectDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(UPDATE_SQL);
            prepareStatement.setString(1, projectDto.getProjectName());
            prepareStatement.setLong(2, projectDto.getCustomerId());
            prepareStatement.setLong(3, id);

            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public boolean delete(Long id) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(DELETE_SQL);
            prepareStatement.setLong(1, id);
            return prepareStatement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}

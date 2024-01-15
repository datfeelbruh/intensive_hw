package com.sobad.dao;

import com.sobad.dto.EmployeeDto;
import com.sobad.entity.Employee;
import com.sobad.entity.Position;
import com.sobad.entity.Project;
import com.sobad.exception.DaoException;
import com.sobad.util.ConnectionManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeDao {
    private final EmployeePositionDao employeePositionDao;
    private static final String DELETE_SQL = """
            DELETE FROM employee USING employee_position
            WHERE employee.employee_id = employee_position.employee_id
            AND employee.employee_id = ?
            """;

    private static final String SAVE_SQL = """
            INSERT INTO employee (first_name, last_name, project_id)
            VALUES (?, ?, ?);
            """;

    private static final String FIND_ALL_SQL = """
            SELECT employee.employee_id,
                   employee.first_name,
                   employee.last_name,
                   p.project_id,
                   p.project_name,
                   pos.position_id,
                   pos.position_name
            FROM employee
                     JOIN project p ON employee.project_id = p.project_id
                     JOIN employee_position e_p ON employee.employee_id = e_p.employee_id
                     JOIN position pos ON e_p.position_id = pos.position_id
            """;

    private static final String UPDATE_SQL = """
            UPDATE employee
            SET first_name = ?,
            last_name = ?,
            project_id = ?
            WHERE employee_id = ?;
            """;

    private static final String FIND_BY_ID_SQL = FIND_ALL_SQL + """
            WHERE employee.employee_id = ?
            """;

    private static final String INIT_SQL = """
            INSERT INTO employee (first_name, last_name, project_id) VALUES
            ('Ivan', 'Ivanov', 1), ('Petr', 'Petrov', 2), ('Mikhail', 'Mishustin', 3),
            ('Oleg', 'Stepanov', 1), ('Dmitriy', 'Tavrin', 2), ('Alex', 'Sergienko', 3),
            ('Kate', 'Kashina', 1), ('Yaroslav', 'Ivashin', 2), ('Pavel', 'Goncharov', 3);
            """;
    private static final String DELETE_ALL_SQL = """
            TRUNCATE employee RESTART IDENTITY CASCADE;
            """;

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

    public Employee save(EmployeeDto employeeDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, employeeDto.getFirstName());
            prepareStatement.setString(2, employeeDto.getLastName());
            prepareStatement.setLong(3, employeeDto.getProjectId());

            prepareStatement.executeUpdate();
            var generatedKeys = prepareStatement.getGeneratedKeys();

            Employee employee = null;
            if (generatedKeys.next()) {
                long employeeId = generatedKeys.getLong("employee_id");
                PreparedStatement updateManyToMany = connection.prepareStatement("""
                        INSERT INTO employee_position (position_id, employee_id) VALUES 
                        (?, ?)
                        """);
                updateManyToMany.setLong(1, employeeDto.getPositionId());
                updateManyToMany.setLong(2, generatedKeys.getLong("employee_id"));
                updateManyToMany.executeUpdate();

                employee = findById(employeeId);
            }
            return employee;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    public Employee findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_BY_ID_SQL);
            prepareStatement.setLong(1, id);

            var resultSet = prepareStatement.executeQuery();
            Employee employee = null;
            if (resultSet.next()) {
                employee = buildEmployee(resultSet);
            }
            return employee;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Employee> findAll() {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL);
            var resultSet = prepareStatement.executeQuery();
            List<Employee> employees = new ArrayList<>();
            while (resultSet.next()) {
                employees.add(buildEmployee(resultSet));
            }
            return employees;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Long id, EmployeeDto employeeDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(UPDATE_SQL);
            prepareStatement.setString(1, employeeDto.getFirstName());
            prepareStatement.setString(2, employeeDto.getLastName());
            prepareStatement.setLong(3, employeeDto.getProjectId());
            prepareStatement.setLong(4, id);

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

    private Employee buildEmployee(ResultSet resultSet) throws SQLException {
        var project = Project.builder()
                .projectId(resultSet.getLong("project_id"))
                .projectName(resultSet.getString("project_name"))
                .build();
        var position = Position.builder()
                .positionId(resultSet.getLong("position_id"))
                .positionName(resultSet.getString("position_name"))
                .build();

        return Employee.builder()
                .id(resultSet.getLong("employee_id"))
                .firstName(resultSet.getString("first_name"))
                .lastName(resultSet.getString("last_name"))
                .project(project)
                .position(position)
                .build();
    }
}

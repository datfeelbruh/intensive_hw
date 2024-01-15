package com.sobad.dao;

import com.sobad.exception.DaoException;
import com.sobad.util.ConnectionManager;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class EmployeePositionDao {
    private static final String INIT_SQL = """
            INSERT INTO employee_position (position_id, employee_id) VALUES 
            (1, 1), (2, 2), (3, 3),
            (2, 4), (3, 5), (1, 6),
            (3, 7), (1, 8), (2, 9);
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
            var prepareStatement = connection.prepareStatement("TRUNCATE employee_position");
            prepareStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
}

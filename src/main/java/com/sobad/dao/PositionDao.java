package com.sobad.dao;

import com.sobad.dto.PositionDto;
import com.sobad.entity.Position;
import com.sobad.exception.DaoException;
import com.sobad.util.ConnectionManager;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class PositionDao {
    private static final String INIT_SQL = """
            INSERT INTO position (position_name) VALUES
            ('PM'), ('Programmer'), ('Analytic');
            """;
    private static final String DELETE_ALL_SQL = """
            TRUNCATE position RESTART IDENTITY CASCADE;
            """;

    private static final String SAVE_SQL = """
            INSERT INTO position (position_name) VALUES
            (?)
            """;

    private static final String FIND_ALL = """
            SELECT position_id, position_name
            FROM position
            """;
    private static final String FIND_BY_ID = """
            WHERE position_id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE position
            SET position_name = ?
            WHERE position_id = ?;
            """;


    private static final String DELETE_SQL = """
            DELETE FROM position
            WHERE position_id = ?
            """;

    public void init() {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(INIT_SQL);
            prepareStatement.execute();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Position save(PositionDto positionDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, positionDto.getPositionName());

            prepareStatement.executeUpdate();
            var generatedKeys = prepareStatement.getGeneratedKeys();

            Position position = null;
            if (generatedKeys.next()) {
                position = Position.builder()
                        .positionId(generatedKeys.getLong("position_id"))
                        .positionName(generatedKeys.getString("position_name"))
                        .build();
            }
            return position;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Position findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL + FIND_BY_ID);
            prepareStatement.setLong(1, id);

            var resultSet = prepareStatement.executeQuery();
            Position position = null;
            if (resultSet.next()) {
                position = Position.builder()
                        .positionId(resultSet.getLong("position_id"))
                        .positionName(resultSet.getString("position_name"))
                        .build();
            }
            return position;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Position> findAll() {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL);

            var resultSet = prepareStatement.executeQuery();
            List<Position> positions = new ArrayList<>();
            while (resultSet.next()) {
                positions.add(Position.builder()
                        .positionId(resultSet.getLong("position_id"))
                        .positionName(resultSet.getString("position_name"))
                        .build());
            }
            return positions;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Long id, PositionDto positionDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(UPDATE_SQL);
            prepareStatement.setString(1, positionDto.getPositionName());
            prepareStatement.setLong(2, id);

            prepareStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void delete(Long id) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(DELETE_SQL);
            prepareStatement.setLong(1, id);

            prepareStatement.executeUpdate();
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
}

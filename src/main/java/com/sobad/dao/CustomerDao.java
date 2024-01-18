package com.sobad.dao;

import com.sobad.dto.CustomerDto;
import com.sobad.entity.Customer;
import com.sobad.exception.DaoException;
import com.sobad.util.ConnectionManager;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDao {

    private static final CustomerDao INSTANCE = new CustomerDao();

    private static final String INIT_SQL = """
            INSERT INTO customer (customer_name) VALUES 
            ('VTB'), ('Arasaka'), ('Starbucks');
            """;

    private static final String SAVE_SQL = """
            INSERT INTO customer (customer_name)
            VALUES (?);
            """;

    private static final String DELETE_ALL_SQL = """
            TRUNCATE customer RESTART IDENTITY CASCADE;
            """;

    private static final String FIND_ALL_SQL = """
            SELECT customer_id,
                   customer_name
            FROM customer
            """;

    private static final String FIND_BY_ID = """
            SELECT customer_id,
                   customer_name
            FROM customer
            WHERE customer.customer_id = ?
            """;

    private static final String UPDATE_SQL = """
            UPDATE customer
            SET customer_name = ?
            WHERE customer_id = ?;
            """;


    private static final String DELETE_SQL = """
            DELETE FROM customer
            WHERE customer_id = ?
            """;

    private CustomerDao() {
    }

    public static CustomerDao getInstance() {
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

    public Customer save(CustomerDto customerDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(SAVE_SQL, Statement.RETURN_GENERATED_KEYS);
            prepareStatement.setString(1, customerDto.getCustomerName());

            prepareStatement.executeUpdate();
            var generatedKeys = prepareStatement.getGeneratedKeys();

            Customer customer = null;
            if (generatedKeys.next()) {
                customer = findById(generatedKeys.getLong("customer_id"));
            }
            return customer;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public Customer findById(Long id) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_BY_ID);
            prepareStatement.setLong(1, id);

            var resultSet = prepareStatement.executeQuery();
            Customer customer = null;
            if (resultSet.next()) {
                customer = Customer.builder()
                        .id(resultSet.getLong("customer_id"))
                        .name(resultSet.getString("customer_name"))
                        .build();
            }
            return customer;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public List<Customer> findAll() {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(FIND_ALL_SQL);

            var resultSet = prepareStatement.executeQuery();
            List<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                customers.add(Customer.builder()
                        .id(resultSet.getLong("customer_id"))
                        .name(resultSet.getString("customer_name"))
                        .build());
            }
            return customers;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    public void update(Long id, CustomerDto customerDto) {
        try (var connection = ConnectionManager.get()) {
            var prepareStatement = connection.prepareStatement(UPDATE_SQL);
            prepareStatement.setString(1, customerDto.getCustomerName());
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

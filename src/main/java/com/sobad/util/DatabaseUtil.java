package com.sobad.util;

import java.sql.SQLException;

public final class DatabaseUtil {
    private static final String INIT_TABLES = """
            CREATE TABLE IF NOT EXISTS customer (
                customer_id SERIAL PRIMARY KEY,
                customer_name varchar(255)
            );
            CREATE TABLE IF NOT EXISTS project (
                project_id SERIAL PRIMARY KEY,
                project_name varchar(255) NOT NULL,
                customer_id BIGINT references customer (customer_id) ON DELETE SET NULL,
                CONSTRAINT project_unique UNIQUE (project_name)
            );
            
            CREATE TABLE IF NOT EXISTS employee (
                employee_id SERIAL PRIMARY KEY,
                first_name varchar(255) NOT NULL,
                last_name varchar(255) NOT NULL,
                project_id BIGINT REFERENCES project (project_id) ON DELETE SET NULL
            );
            CREATE TABLE IF NOT EXISTS position (
                position_id SERIAL PRIMARY KEY,
                position_name varchar(255),
                CONSTRAINT position_unique UNIQUE (position_name)
            );
            
            CREATE TABLE IF NOT EXISTS employee_position (
                id SERIAL PRIMARY KEY,
                position_id BIGINT NOT NULL REFERENCES position(position_id) ON DELETE CASCADE,
                employee_id BIGINT NOT NULL REFERENCES employee (employee_id) ON DELETE CASCADE
            );
            """;

    public static void initTables() {
        try (var connection = ConnectionManager.get();
             var statement = connection.createStatement()) {
             statement.execute(INIT_TABLES);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

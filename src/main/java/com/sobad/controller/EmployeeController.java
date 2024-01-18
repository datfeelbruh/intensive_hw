package com.sobad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sobad.dao.EmployeeDao;
import com.sobad.dao.EmployeePositionDao;
import com.sobad.dto.EmployeeDto;
import com.sobad.entity.Employee;
import com.sobad.service.EmployeeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/employee/*")
public class EmployeeController extends HttpServlet {
    private final EmployeeService employeeService = EmployeeService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            List<Employee> all = employeeService.findAll();
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write(mapper.writeValueAsString(all));
            writer.flush();
        } else {
            String[] split = pathInfo.split("/");
            Long id = Long.parseLong(split[1]);
            Employee byId = employeeService.findById(id);
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write(mapper.writeValueAsString(byId));
            writer.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        EmployeeDto employeeDto = mapper.readValue(sb.toString(), EmployeeDto.class);
        Employee employee = employeeService.create(employeeDto);

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(mapper.writeValueAsString(employee));
        writer.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            throw new RuntimeException();
        }
        String[] split = pathInfo.split("/");
        Long id = Long.parseLong(split[1]);

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = req.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        EmployeeDto employeeDto = mapper.readValue(sb.toString(), EmployeeDto.class);
        employeeService.update(id, employeeDto);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            throw new RuntimeException();
        }
        String[] split = pathInfo.split("/");
        Long id = Long.parseLong(split[1]);
        employeeService.delete(id);
    }
}

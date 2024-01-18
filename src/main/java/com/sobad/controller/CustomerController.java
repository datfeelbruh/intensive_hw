package com.sobad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sobad.dto.CustomerDto;
import com.sobad.dto.CustomerReadDto;
import com.sobad.service.CustomerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/customer/*")
public class CustomerController extends HttpServlet {
    private final CustomerService customerService = CustomerService.getInstance();

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            List<CustomerReadDto> all = customerService.findAll();
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write(mapper.writeValueAsString(all));
            writer.flush();
        } else {
            String[] split = pathInfo.split("/");
            Long id = Long.parseLong(split[1]);
            CustomerReadDto byId = customerService.findById(id);
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
        CustomerDto employeeDto = mapper.readValue(sb.toString(), CustomerDto.class);
        CustomerReadDto customerReadDto = customerService.create(employeeDto);

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(mapper.writeValueAsString(customerReadDto));
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
        CustomerDto customerDto = mapper.readValue(sb.toString(), CustomerDto.class);
        customerService.update(id, customerDto);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            throw new RuntimeException();
        }
        String[] split = pathInfo.split("/");
        Long id = Long.parseLong(split[1]);
        customerService.delete(id);
    }
}
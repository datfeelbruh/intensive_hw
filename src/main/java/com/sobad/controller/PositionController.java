package com.sobad.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sobad.dto.EmployeeDto;
import com.sobad.dto.PositionDto;
import com.sobad.entity.Employee;
import com.sobad.entity.Position;
import com.sobad.service.PositionService;
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

@WebServlet("/api/position/*")
public class PositionController extends HttpServlet {
    private final PositionService positionService = PositionService.getInstance();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            List<Position> all = positionService.findAll();
            PrintWriter writer = resp.getWriter();
            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.setStatus(HttpServletResponse.SC_OK);
            writer.write(mapper.writeValueAsString(all));
            writer.flush();
        } else {
            String[] split = pathInfo.split("/");
            Long id = Long.parseLong(split[1]);
            Position byId = positionService.findById(id);
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
        PositionDto positionDto = mapper.readValue(sb.toString(), PositionDto.class);
        Position position = positionService.create(positionDto);

        PrintWriter writer = resp.getWriter();
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(HttpServletResponse.SC_CREATED);
        writer.write(mapper.writeValueAsString(position));
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
        PositionDto positionDto = mapper.readValue(sb.toString(), PositionDto.class);
        positionService.update(id, positionDto);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            throw new RuntimeException();
        }
        String[] split = pathInfo.split("/");
        Long id = Long.parseLong(split[1]);
        positionService.delete(id);
    }
}

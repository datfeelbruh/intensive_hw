package com.sobad.controller;

import com.sobad.dao.*;
import com.sobad.service.InitService;
import com.sobad.util.DatabaseUtil;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@WebServlet("/api/util")
public class InitController extends HttpServlet {

    private final InitService initService = InitService.getInstance();

    @Override
    public void init(ServletConfig config) throws ServletException {
        DatabaseUtil.initTables();
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        initService.init();
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
       initService.clear();
    }
}

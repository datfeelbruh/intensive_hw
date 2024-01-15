package com.sobad.controller;

import com.sobad.service.InitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/util")
@RequiredArgsConstructor
public class InitController {
    private final InitService initService;

    @PostMapping()
    public void getAll() {
        initService.init();
    }

    @DeleteMapping()
    public void clearAll() {
        initService.clear();
    }
}

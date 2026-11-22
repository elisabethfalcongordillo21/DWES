package com.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.proyecto.model.Log;
import com.proyecto.repository.LogRepository;

@Controller
@RequestMapping("/logs")
public class LogController {

    @Autowired
    LogRepository repoLog;

    // Historial de acciones con lo más nuevo primero
    @GetMapping
    public String lista(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size, Model model) {

        Page<Log> logs = repoLog.findAllByOrderByFechaDesc(PageRequest.of(page, size));

        model.addAttribute("logs", logs);
        model.addAttribute("size", size);

        return "logs/lista";
    }
}
package com.proyecto.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.model.Log;
import com.proyecto.repository.LogRepository;

@Service
public class LogService {

    @Autowired
    LogRepository repoLog;

    public void registrar(String accion, String entidad, String descripcion) {
        Log log = new Log();
        log.setAccion(accion);
        log.setEntidad(entidad);
        log.setDescripcion(descripcion);
        log.setFecha(LocalDateTime.now());
        repoLog.save(log);
    }
}
package com.proyecto.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.model.Log;

public interface LogRepository extends JpaRepository<Log, Long> {

    // Los logs más recientes primero paginados
    Page<Log> findAllByOrderByFechaDesc(Pageable pageable);
}
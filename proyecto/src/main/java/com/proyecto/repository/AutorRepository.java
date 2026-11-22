package com.proyecto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.model.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long> {

    // Borra todos los autores de una localidad
    @Transactional
    void deleteByLocalidad(String localidad);

    // Borra los autores nacidos antes de una fecha
    @Transactional
    void deleteByFechaNacimientoBefore(LocalDate fecha);

    // Nombre que contenga un texto y apellidos que contengan otro
    List<Autor> findByNombreContainingIgnoreCaseAndApellidosContainingIgnoreCase(String nombre,String apellidos);

    // Autores que pertenecen a una agrupación
    @Query("SELECT a FROM Autor a JOIN a.agrupaciones g WHERE g.id = :agrupacionId")
    List<Autor> findByAgrupacionId(@Param("agrupacionId") Long agrupacionId);

    // Autores que no pertenecen todavía a esa agrupación
    @Query("SELECT a FROM Autor a WHERE a.id NOT IN "
            + "(SELECT a2.id FROM Autor a2 JOIN a2.agrupaciones g WHERE g.id = :agrupacionId)")
    List<Autor> findNoAsociadosA(@Param("agrupacionId") Long agrupacionId);
}
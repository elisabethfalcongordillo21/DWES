package com.proyecto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.model.Agrupacion;

public interface AgrupacionRepository extends JpaRepository<Agrupacion, Long> {

    // Agrupaciones de una localidad y de una modalidad concreta
    List<Agrupacion> findByLocalidadAndModalidadId(String localidad, Long modalidadId);

    // Nombre que contenga un texto y localidad que contenga otro 
    List<Agrupacion> findByNombreContainingIgnoreCaseAndLocalidadContainingIgnoreCase(String nombre, String localidad);

    // Todas las agrupaciones ordenadas por nombre de la A a la Z
    List<Agrupacion> findAllByOrderByNombreAsc();

    // Cuántas agrupaciones hay en una modalidad
    long countByModalidadId(Long modalidadId);

    // Las 5 agrupaciones más antiguas (para Estadísticas)
    List<Agrupacion> findTop5ByOrderByAnioFundacionAsc();

    // Busca un texto en el nombre o en la descripción
    @Query("SELECT a FROM Agrupacion a WHERE LOWER(a.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) " + "OR LOWER(a.descripcion) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Agrupacion> buscarPorTexto(@Param("texto") String texto);

}
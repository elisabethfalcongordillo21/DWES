package com.proyecto.proyecto.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Concurso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer anio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String teatro;

    // 1:N -> un concurso incluye muchas participaciones
    @OneToMany(mappedBy = "concurso")
    private List<Participacion> participaciones = new ArrayList<>();
}

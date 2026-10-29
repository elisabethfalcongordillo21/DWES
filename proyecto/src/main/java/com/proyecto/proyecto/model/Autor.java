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
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellidos;
    private String apodo;
    private LocalDate fechaNacimiento;
    private String localidad;

    @Column(length = 3000)
    private String biografia;

    // N:M con Agrupacion ("escriben") -> tabla intermedia autor_agrupacion
    @ManyToMany
    @JoinTable(name = "autor_agrupacion",
            joinColumns = @JoinColumn(name = "autor_id"),
            inverseJoinColumns = @JoinColumn(name = "agrupacion_id"))
    private List<Agrupacion> agrupaciones = new ArrayList<>();

    // N:M con Cancion ("componen") -> tabla intermedia autor_cancion
    @ManyToMany
    @JoinTable(name = "autor_cancion",
            joinColumns = @JoinColumn(name = "autor_id"),
            inverseJoinColumns = @JoinColumn(name = "cancion_id"))
    private List<Cancion> canciones = new ArrayList<>();
}

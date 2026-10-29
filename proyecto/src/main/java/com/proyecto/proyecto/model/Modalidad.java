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
public class Modalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;

    // 1:N -> una modalidad tiene muchas agrupaciones
    @OneToMany(mappedBy = "modalidad")
    private List<Agrupacion> agrupaciones = new ArrayList<>();
}

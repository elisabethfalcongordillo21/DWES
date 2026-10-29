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
public class Agrupacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(length = 2000)
    private String descripcion;

    private String localidad;
    private Integer anioFundacion;
    private String web;
    private String fotoUrl;

    // N:1 -> muchas agrupaciones pertenecen a una modalidad
    @ManyToOne
    @JoinColumn(name = "modalidad_id")
    private Modalidad modalidad;

    // 1:1 -> una agrupacion tiene un tipo (la FK esta en Tipo)
    @OneToOne(mappedBy = "agrupacion", cascade = CascadeType.ALL)
    private Tipo tipo;

    // 1:N -> una agrupacion tiene muchas participaciones
    @OneToMany(mappedBy = "agrupacion")
    private List<Participacion> participaciones = new ArrayList<>();

    // N:M -> lado inverso (el dueño es Autor)
    @ManyToMany(mappedBy = "agrupaciones")
    private List<Autor> autores = new ArrayList<>();
}

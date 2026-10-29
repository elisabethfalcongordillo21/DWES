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
public class Tipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String disenador;

    @OneToOne
    @JoinColumn(name = "agrupacion_id")
    private Agrupacion agrupacion;
}

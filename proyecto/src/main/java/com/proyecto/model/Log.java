package com.proyecto.model;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "logs")
@Getter
@Setter
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accion; // CREAR, MODIFICAR, ELIMINAR, ASOCIAR, DESASOCIAR
    private String entidad; // Agrupacion, Autor...

    @Column(length = 500)
    private String descripcion;

    private LocalDateTime fecha;
}
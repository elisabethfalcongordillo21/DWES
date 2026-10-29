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
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String tipo;

    @Column(length = 5000)
    private String letra;

    private Integer duracion;

    // FK participacion_id tal como aparece en el ER
    @ManyToOne
    @JoinColumn(name = "participacion_id")
    private Participacion participacion;

    // N:1 -> muchas canciones tienen un autor (FK autor_id)
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    // 1:N -> una cancion recibe muchas valoraciones
    @OneToMany(mappedBy = "cancion")
    private List<Valoracion> valoraciones = new ArrayList<>();
}

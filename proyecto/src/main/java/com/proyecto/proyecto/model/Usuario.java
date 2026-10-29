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
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private LocalDate fechaRegistro;

    // 1:N -> un usuario hace muchas valoraciones
    @OneToMany(mappedBy = "usuario")
    private List<Valoracion> valoraciones = new ArrayList<>();
}

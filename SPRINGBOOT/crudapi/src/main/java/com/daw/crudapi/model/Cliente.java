package com.daw.crudapi.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_email", columnList = "email"))
public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = true)
    private int edad;

    @Column(nullable = false)
    private String telefono;

    // En mappedBy va el nombre del campo de la otra entidad
    // Que sirve para mapear esta
    @ToString.Exclude
    @OneToMany(mappedBy = "cliente")
    private List<Direccion> direcciones = new ArrayList<>();

    public void addDireccion(Direccion direccion) {
        // Le decimos que la direccion pertenece a este cliente
        direccion.setCliente(this);
        // Añadimos la direccion a la lista de direcciones del cliente
        direcciones.add(direccion);

    }

    @ToString.Exclude
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY)
    private List<Pedido> pedidos = new ArrayList<>();

    /**
     * Funcion para añadir correctamente un pedido a este cliente
     * 
     * @param pedido
     */
    public void addPedido(Pedido pedido) {
        // Añadimos el pedido a la lista de pedidos del cliente
        pedidos.add(pedido);
        // Le decimos que el pedido pertenece a este cliente
        pedido.setCliente(this);
    }

}
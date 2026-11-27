package com.daw.crudapi.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fechaPedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado;

    // Forzamos a que cargue la direccion cuando carga el pedido
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "direccion_id", nullable = false)
    private Direccion direccion;

    // Forzamos a que cargue el cliente cuando carga el pedido
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;


    // apartado 2- relacion pedido paquete
    @ToString.Exclude
    @ManyToMany(cascade = CascadeType.REMOVE, mappedBy = "pedido", orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Paquete> paquetes = new ArrayList<>();

    
    /**
     * Funcion para añadir correctamente un producto a este pedido
     * 
     * @param producto
     */
    public void addProducto(Producto producto) {
        // Añadimos el producto a la lista de productos del pedido
        productos.add(producto);
        // Le decimos que el producto pertenece a este pedido
        producto.setPedido(this);
    }

}

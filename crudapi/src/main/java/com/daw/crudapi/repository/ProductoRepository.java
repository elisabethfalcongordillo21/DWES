package com.daw.crudapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.crudapi.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

}

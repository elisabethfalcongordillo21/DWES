package com.daw.crudapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.daw.crudapi.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

Optional<Cliente> findByEmail(String email);

//Muestra los 8 clientes mas jovenes 
List<Cliente> findTop8By(Sort sort);

//Existe un cliente con menos de n años
boolean  existsByEdadLessThanEqual(int edad);

}
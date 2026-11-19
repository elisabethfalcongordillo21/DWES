package com.daw.crudapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.autoconfigure.web.DataWebProperties.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.daw.crudapi.model.Cliente;
import com.daw.crudapi.repository.ClienteRepository;


@Controller 
@RequestMapping("/cliente") 
public class MainController {

    @Autowired 
    ClienteRepository repoCli;

    @GetMapping("lista")
    public String listaClientes(@RequestParam(defaultValue = "0") int pagina,Model model) {
       
        Pageable pageable  = PageRequest.of(pagina, 10);
        //Saco de la bd todos los clientes
        Page<Cliente> clientes =  repoCli.findAll(pageable);

        //Para que le llegen los datos a la vista meto en el model 
        model.addAttribute("clientes",clientes);

        return "listaClientes";
    }

    @PostMapping ("cliente/insertar")
    public String insertarCliente()
    {
        return "";
    }

    //Si se llama al endpoint con la url "cliente/6/eliminar"  se mapea el 6 en la variable id
    // y sabremos el id del cliente a eliminar
    @DeleteMapping("/{id}/eliminar")
    public String eliminarCliente(@PathVariable long id)
    {
        //borramos el cliente
        repoCli.deleteById(id);
        System.out.println("El cliente con id " + id);

        return "redirect:/cliente";
    } 
    
}

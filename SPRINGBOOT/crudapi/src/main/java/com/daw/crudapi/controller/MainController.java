package com.daw.crudapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.crudapi.model.Cliente;
import com.daw.crudapi.repository.ClienteRepository;

@Controller
@RequestMapping("/cliente")
public class MainController {

    @Autowired
    ClienteRepository repoCli;

    @GetMapping("lista")
    public String listaClientes(@RequestParam(defaultValue = "0") int pagina, Model model) {

        Pageable pageable = PageRequest.of(pagina, 10);
        // Saco de la bd todos los clientes
        Page<Cliente> clientes = repoCli.findAll(pageable);

        // Para que le llegen los datos a la vista meto en el model
        model.addAttribute("clientes", clientes);

        return "listaClientes";
    }


    // Si se llama al endpoint con la url "cliente/6/eliminar" se mapea el 6 en la
    // variable id
    // y sabremos el id del cliente a eliminar
    @DeleteMapping("/{id}/eliminar")
    public String eliminarCliente(@PathVariable long id) {

        Cliente cliente = repoCli.findById(id).orElseThrow();

        cliente.getPedidos().clear(); // orphanRemoval marca los pedidos para borrar
        repoCli.flush(); // ejecuta ya los DELETE de pedidos

        // borramos el cliente
        repoCli.deleteById(id);

        return "redirect:/cliente/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model)
    {
        //Creamos un registro nuevo para que se mapee con el formulario
        Cliente nuevo = new Cliente();
        nuevo.setEdad(18);
        //Usamos el model para transportar el entity hacia la vista del formulario
        //de insercion
        model.addAttribute("cliente",nuevo);

        return "FormularioCliente";
    }

    @PostMapping("/insertar")
    public String insertarCliente(@ModelAttribute Cliente cliente, Model model, RedirectAttributes ra)
    {

        //Si ya existe ese email no se puede repetir damos fallo
        if (repoCli.findByEmail(cliente.getEmail()).isPresent())
        {
            model.addAttribute("error", "Ya existe un cliente con ese email");
            return "FormularioCliente";
        }

        //Guardamos al cliente
        repoCli.save(cliente);

        //para pasar datos a otro endpoint del controlador usamos redirectAttributes en lugar de Model
        ra.addFlashAttribute("sucess", "El cliente " + cliente.getNombre() + " ha sido insertado correctamente");
        return "redirect:/cliente/lista";

    }


}

package com.proyecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.proyecto.repository.AgrupacionRepository;
import com.proyecto.repository.AutorRepository;
import com.proyecto.repository.ModalidadRepository;

@Controller
@RequestMapping("/busqueda")
public class BusquedaController {

    @Autowired
    AgrupacionRepository repoAgr;
    @Autowired
    AutorRepository repoAut;
    @Autowired
    ModalidadRepository repoMod;

    // La página con los formularios y muestra los resultados, el valor "tipo" dice cuál de los 4 formularios se ha enviado
    @GetMapping
    public String buscar(@RequestParam(required = false) String tipo,
            @RequestParam(defaultValue = "") String nombre,
            @RequestParam(defaultValue = "") String localidad,
            @RequestParam(required = false) Long modalidadId,
            @RequestParam(defaultValue = "") String texto,
            @RequestParam(defaultValue = "") String apellidos,
            Model model) {

        // Para el desplegable de modalidades
        model.addAttribute("modalidades", repoMod.findAll());

        if (tipo != null) {
            model.addAttribute("buscado", true);

            if (tipo.equals("agrupacion")) {
                model.addAttribute("agrupaciones",
                        repoAgr.findByNombreContainingIgnoreCaseAndLocalidadContainingIgnoreCase(nombre, localidad));

            } else if (tipo.equals("modalidad") && modalidadId != null) {
                model.addAttribute("agrupaciones", repoAgr.findByLocalidadAndModalidadId(localidad, modalidadId));

            } else if (tipo.equals("texto")) {
                model.addAttribute("agrupaciones", repoAgr.buscarPorTexto(texto));

            } else if (tipo.equals("autor")) {
                model.addAttribute("autores",
                        repoAut.findByNombreContainingIgnoreCaseAndApellidosContainingIgnoreCase(nombre,
                                apellidos));
            }
        }

        return "busqueda/busqueda";
    }
}
package com.proyecto.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.proyecto.model.Modalidad;
import com.proyecto.repository.AgrupacionRepository;
import com.proyecto.repository.AutorRepository;
import com.proyecto.repository.ModalidadRepository;

@Controller
@RequestMapping("/estadisticas")
public class EstadisticasController {

    @Autowired
    AgrupacionRepository repoAgr;
    @Autowired
    AutorRepository repoAut;
    @Autowired
    ModalidadRepository repoMod;

    @GetMapping
    public String estadisticas(Model model) {

        // Totales
        model.addAttribute("totalAgrupaciones", repoAgr.count());
        model.addAttribute("totalAutores", repoAut.count());

        // Conteo: número de agrupaciones de cada modalidad
        Map<String, Long> porModalidad = new LinkedHashMap<>();
        long maximo = 1; // el número más alto, para dibujar las barras (mínimo 1 para no dividir entre 0)
        for (Modalidad modalidad : repoMod.findAll()) {
            long cantidad = repoAgr.countByModalidadId(modalidad.getId());
            porModalidad.put(modalidad.getNombre(), cantidad);
            if (cantidad > maximo) {
                maximo = cantidad;
            }
        }
        model.addAttribute("porModalidad", porModalidad);
        model.addAttribute("maximo", maximo);

        // Agregación,las 5 agrupaciones más antiguas
        model.addAttribute("masAntiguas", repoAgr.findTop5ByOrderByAnioFundacionAsc());

        return "estadisticas/estadisticas";
    }
}
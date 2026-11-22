package com.proyecto.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.proyecto.model.Agrupacion;
import com.proyecto.model.Autor;
import com.proyecto.model.Modalidad;
import com.proyecto.repository.AgrupacionRepository;
import com.proyecto.repository.AutorRepository;
import com.proyecto.repository.ModalidadRepository;
import com.proyecto.service.LogService;

@Controller
@RequestMapping("/agrupacion")
public class AgrupacionController {

    @Autowired
    AgrupacionRepository repoAgr;
    @Autowired
    AutorRepository repoAut;
    @Autowired
    ModalidadRepository repoMod;
    @Autowired
    LogService logService;

    // GET /agrupacion?page=0&size=10 -> listado paginado
    @GetMapping
    public String lista(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre"));
        Page<Agrupacion> agrupaciones = repoAgr.findAll(pageable);

        model.addAttribute("agrupaciones", agrupaciones);
        model.addAttribute("size", size);

        return "agrupacion/lista";
    }

    // GET /agrupacion/nuevo -> formulario vacío
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("agrupacion", new Agrupacion());
        model.addAttribute("modalidades", repoMod.findAll());
        return "agrupacion/nuevo";
    }

    // POST /agrupacion/crear -> guarda la agrupación nueva
    @PostMapping("/crear")
    public String crear(@ModelAttribute Agrupacion agrupacion, RedirectAttributes ra) {

        agrupacion.setModalidad(buscarModalidad(agrupacion));
        repoAgr.save(agrupacion);

        logService.registrar("CREAR", "Agrupacion", agrupacion.getNombre());
        ra.addFlashAttribute("success", "La agrupación " + agrupacion.getNombre() + " se ha creado correctamente");
        return "redirect:/agrupacion";
    }

    // GET /agrupacion/editar/{id} -> el mismo formulario, pero con los datos cargados
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Agrupacion agrupacion = repoAgr.findById(id).orElseThrow();
        model.addAttribute("agrupacion", agrupacion);
        model.addAttribute("modalidades", repoMod.findAll());
        return "agrupacion/editar";
    }

    // POST /agrupacion/modificar -> guarda los cambios
    @PostMapping("/modificar")
    public String modificar(@ModelAttribute Agrupacion datos, RedirectAttributes ra) {

        // Cargamos la agrupación real de la BD y solo le copiamos los campos del formulario.
        // Así no perdemos sus autores, participaciones ni su tipo.
        Agrupacion agrupacion = repoAgr.findById(datos.getId()).orElseThrow();
        agrupacion.setNombre(datos.getNombre());
        agrupacion.setDescripcion(datos.getDescripcion());
        agrupacion.setLocalidad(datos.getLocalidad());
        agrupacion.setAnioFundacion(datos.getAnioFundacion());
        agrupacion.setFotoUrl(datos.getFotoUrl());
        agrupacion.setModalidad(buscarModalidad(datos));

        repoAgr.save(agrupacion);

        logService.registrar("MODIFICAR", "Agrupacion", agrupacion.getNombre());
        ra.addFlashAttribute("success", "La agrupación " + agrupacion.getNombre() + " se ha modificado correctamente");
        return "redirect:/agrupacion";
    }

    // GET /agrupacion/eliminar/{id} -> se llega desde el modal de confirmación
    @GetMapping("/eliminar/{id}")
    @Transactional
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {

        Agrupacion agrupacion = repoAgr.findById(id).orElseThrow();

        // Si ha participado en concursos no la borramos
        if (!agrupacion.getParticipaciones().isEmpty()) {
            ra.addFlashAttribute("error", "No se puede eliminar " + agrupacion.getNombre()
                    + " porque tiene participaciones en concursos");
            return "redirect:/agrupacion";
        }

        // Quitamos la agrupación de la lista de cada autor (tabla intermedia autor_agrupacion)
        for (Autor autor : new ArrayList<>(agrupacion.getAutores())) {
            autor.getAgrupaciones().remove(agrupacion);
        }

        // Su Tipo se borra solo (cascade = ALL)
        repoAgr.delete(agrupacion);

        logService.registrar("ELIMINAR", "Agrupacion", agrupacion.getNombre());
        ra.addFlashAttribute("success", "La agrupación " + agrupacion.getNombre() + " se ha eliminado");
        return "redirect:/agrupacion";
    }

    // GET /agrupacion/detalle/{id} -> datos de la agrupación + tabla de sus autores
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable Long id, Model model, RedirectAttributes ra) {

        Agrupacion agrupacion = repoAgr.findById(id).orElse(null);

        // Si el id no existe (por ejemplo, porque se borró), volvemos al listado con un aviso
        if (agrupacion == null) {
            ra.addFlashAttribute("error", "La agrupación con id " + id + " no existe");
            return "redirect:/agrupacion";
        }

        model.addAttribute("agrupacion", agrupacion);
        // Solo los autores de ESTA agrupación
        model.addAttribute("autores", repoAut.findByAgrupacionId(id));
        // Autores que aún no están en ella (para el desplegable de "asociar")
        model.addAttribute("disponibles", repoAut.findNoAsociadosA(id));

        return "agrupacion/detalle";
    }

    // El formulario solo envía el id de la modalidad; aquí buscamos la modalidad completa.
    // Si no eligió ninguna, devolvemos null.
    private Modalidad buscarModalidad(Agrupacion agrupacion) {
        if (agrupacion.getModalidad() == null || agrupacion.getModalidad().getId() == null) {
            return null;
        }
        return repoMod.findById(agrupacion.getModalidad().getId()).orElse(null);
    }
}
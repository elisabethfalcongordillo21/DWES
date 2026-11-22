package com.proyecto.controller;

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
import com.proyecto.repository.AgrupacionRepository;
import com.proyecto.repository.AutorRepository;
import com.proyecto.service.LogService;

@Controller
@RequestMapping("/autor")
public class AutorController {

    @Autowired
    AutorRepository repoAut;
    @Autowired
    AgrupacionRepository repoAgr;
    @Autowired
    LogService logService;

    // listado paginado
    @GetMapping
    public String lista(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre"));
        Page<Autor> autores = repoAut.findAll(pageable);

        model.addAttribute("autores", autores);
        model.addAttribute("size", size);

        return "autor/lista";
    }

  
    // Si se accede desde el detalle de la agrupación, llega el id en "agrupacionId"
    @GetMapping("/nuevo")
    public String nuevo(@RequestParam(required = false) Long agrupacionId, Model model) {
        model.addAttribute("autor", new Autor());
        model.addAttribute("agrupacionId", agrupacionId);
        return "autor/nuevo";
    }

    //Se guarda el autor y si viene de una agrupación lo asocia a ella
    @PostMapping("/crear")
    public String crear(@ModelAttribute Autor autor, @RequestParam(required = false) Long agrupacionId,
            RedirectAttributes ra) {

        if (agrupacionId != null) {
            Agrupacion agrupacion = repoAgr.findById(agrupacionId).orElseThrow();
            autor.getAgrupaciones().add(agrupacion);
        }
        repoAut.save(autor);

        logService.registrar("CREAR", "Autor", autor.getNombre() + " " + autor.getApellidos());
        ra.addFlashAttribute("success", "El autor " + autor.getNombre() + " se ha creado correctamente");
        return volver(agrupacionId);
    }

    
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, @RequestParam(required = false) Long agrupacionId, Model model) {
        model.addAttribute("autor", repoAut.findById(id).orElseThrow());
        model.addAttribute("agrupacionId", agrupacionId);
        return "autor/editar";
    }

    // guarda los cambios
    @PostMapping("/modificar")
    public String modificar(@ModelAttribute Autor datos, @RequestParam(required = false) Long agrupacionId,
            RedirectAttributes ra) {

        // Cargamos el autor real y solo le copiamos los campos del formulario
        // (así no perdemos sus agrupaciones ni sus canciones)
        Autor autor = repoAut.findById(datos.getId()).orElseThrow();
        autor.setNombre(datos.getNombre());
        autor.setApellidos(datos.getApellidos());
        autor.setApodo(datos.getApodo());
        autor.setFechaNacimiento(datos.getFechaNacimiento());
        autor.setLocalidad(datos.getLocalidad());
        autor.setBiografia(datos.getBiografia());

        repoAut.save(autor);

        logService.registrar("MODIFICAR", "Autor", autor.getNombre() + " " + autor.getApellidos());
        ra.addFlashAttribute("success", "El autor " + autor.getNombre() + " se ha modificado correctamente");
        return volver(agrupacionId);
    }

    // Se llega desde el modal de confirmación
    @GetMapping("/eliminar/{id}")
    @Transactional
    public String eliminar(@PathVariable Long id, @RequestParam(required = false) Long agrupacionId,
            RedirectAttributes ra) {

        Autor autor = repoAut.findById(id).orElseThrow();

        // Si tiene canciones no lo borramos
        if (!autor.getCanciones().isEmpty()) {
            ra.addFlashAttribute("error", "No se puede eliminar a " + autor.getNombre()
                    + " porque tiene canciones asociadas");
            return volver(agrupacionId);
        }

        // Las filas de la tabla intermedia autor_agrupacion se borran solas
        repoAut.delete(autor);

        logService.registrar("ELIMINAR", "Autor", autor.getNombre() + " " + autor.getApellidos());
        ra.addFlashAttribute("success", "El autor " + autor.getNombre() + " se ha eliminado");
        return volver(agrupacionId);
    }

    // Añade un autor existente a una agrupación
    @PostMapping("/asociar")
    @Transactional
    public String asociar(@RequestParam Long autorId, @RequestParam Long agrupacionId, RedirectAttributes ra) {

        Autor autor = repoAut.findById(autorId).orElseThrow();
        Agrupacion agrupacion = repoAgr.findById(agrupacionId).orElseThrow();

        if (!autor.getAgrupaciones().contains(agrupacion)) {
            autor.getAgrupaciones().add(agrupacion);
        }

        logService.registrar("ASOCIAR", "Autor", autor.getNombre() + " " + autor.getApellidos() + " -> " + agrupacion.getNombre());
        ra.addFlashAttribute("success", autor.getNombre() + " se ha asociado a " + agrupacion.getNombre());
        return volver(agrupacionId);
    }

    // Quita al autor de la agrupación pero no lo borra
    @GetMapping("/desasociar")
    @Transactional
    public String desasociar(@RequestParam Long autorId, @RequestParam Long agrupacionId, RedirectAttributes ra) {

        Autor autor = repoAut.findById(autorId).orElseThrow();
        Agrupacion agrupacion = repoAgr.findById(agrupacionId).orElseThrow();

        autor.getAgrupaciones().remove(agrupacion);

        logService.registrar("DESASOCIAR", "Autor", autor.getNombre() + " " + autor.getApellidos() + " -> " + agrupacion.getNombre());
        ra.addFlashAttribute("success", autor.getNombre() + " se ha desasociado de " + agrupacion.getNombre());
        return volver(agrupacionId);
    }

    // Si venimos del detalle de una agrupación volvemos allí pero si no al listado de autores
    private String volver(Long agrupacionId) {
        if (agrupacionId != null) {
            return "redirect:/agrupacion/detalle/" + agrupacionId;
        }
        return "redirect:/autor";
    }
}
package com.daw.crudapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.crudapi.model.Paquete;
import com.daw.crudapi.model.Pedido;
import com.daw.crudapi.repository.PaqueteRepository;

@Controller
@RequestMapping("/pedido")
public class PedidoController {

    @Autowired
    PaqueteRepository repoPaq;

    @GetMapping("lista")
    public String listaPedidos(@RequestParam(defaultValue = "0") int pagina, Model model) {

        Pageable pageable = PageRequest.of(pagina, 10, Sort.by("id"));
        Page<Pedido> pedidos = repoPaq.findAll(pageable);

        model.addAttribute("pedidos", pedido);

        return "listaPedidos";
    }

    @GetMapping("/{id}/paquetes")
    public String buscarPedido(@PathVariable long id, @RequestParam(defaultValue = "0") int pagina,
            RedirectAttributes ra) {

        if (!repoPaq.existsById(id)) {
            ra.addFlashAttribute("error", "El producto con id " + id + " no existe");
        } else {
            model.paquetesPedido;
        }

      
    }

  

}

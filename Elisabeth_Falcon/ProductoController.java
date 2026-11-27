package com.daw.crudapi.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.daw.crudapi.model.Pedido;
import com.daw.crudapi.model.Producto;
import com.daw.crudapi.repository.PedidoRepository;
import com.daw.crudapi.repository.ProductoRepository;

@Controller
@RequestMapping("/producto")
public class ProductoController {

    @Autowired
    ProductoRepository repoProd;
    @Autowired
    PedidoRepository repoPed;

    @GetMapping("lista")
    public String listaProductos(@RequestParam(defaultValue = "0") int pagina, Model model) {

        Pageable pageable = PageRequest.of(pagina, 10);
        // Saco de la bd todos los productos
        Page<Producto> productos = repoProd.findAll(pageable);

        // Para que le llegen los datos a la vista meto en el model
        model.addAttribute("productos", productos);

        return "listaProductos";
    }

    // Si se llama al endpoint con la url "producto/6/eliminar" se mapea el 6 en la
    // variable id
    // y sabremos el id del producto a eliminar
    @DeleteMapping("/{id}/eliminar")
    public String eliminarProducto(@PathVariable long id, RedirectAttributes ra) {

        if (!repoProd.existsById(id)) {
            ra.addFlashAttribute("error", "El producto con id " + id + " no existe");
            return "redirect:/producto/lista";
        }

        // borramos el producto
        repoProd.deleteById(id);

        ra.addFlashAttribute("success", "Producto eliminado correctamente");
        return "redirect:/producto/lista";
    }

    @GetMapping("/editar/{id}")
    public String editarProducto(@PathVariable long id, Model model, RedirectAttributes ra) {

        // Cargamos el producto asociado al id
        Optional<Producto> opProd = repoProd.findById(id);

        if (!opProd.isPresent()) {
            ra.addFlashAttribute("error", "El producto con id " + id + " no existe");
            return "redirect:/producto/lista";
        }

        // Pasamos la entity de producto cargada con los datos al formulario
        model.addAttribute("producto", opProd.get());
        // Y los pedidos para poder elegir a cual pertenece
        model.addAttribute("pedidos", repoPed.findAll());

        return "FormularioProducto";
    }

    @PutMapping("/{id}/actualizar")
    public String actualizarProducto(@PathVariable long id, Model model, @ModelAttribute Producto producto,
            @RequestParam Long pedidoId, RedirectAttributes ra) {

        Optional<Producto> antiguo = repoProd.findById(id);
        Optional<Pedido> pedido = repoPed.findById(pedidoId);

        if (antiguo.isEmpty() || pedido.isEmpty()) {
            model.addAttribute("error", "El producto o el pedido no existe");
            model.addAttribute("pedidos", repoPed.findAll());
            return "FormularioProducto";
        }

        // Nos aseguramos de actualizar el producto que toca y le asignamos su pedido
        producto.setId(id);
        producto.setPedido(pedido.get());

        // Actualizamos el producto
        repoProd.save(producto);

        // Si todo ha ido bien enviamos un mensaje y cargamos la vista
        ra.addFlashAttribute("success", "Producto modificado correctamente");
        return "redirect:/producto/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {

        // Creamos un registro nuevo para que se mapee con el formulario
        Producto nuevo = new Producto();
        // Usamos el model para transportar el entity hacia la vista del formulario
        // de insercion
        model.addAttribute("producto", nuevo);
        // Y los pedidos para poder elegir a cual pertenece
        model.addAttribute("pedidos", repoPed.findAll());

        return "FormularioProducto";
    }

    @PostMapping("/insertar")
    public String insertarProducto(@ModelAttribute Producto producto, @RequestParam Long pedidoId, Model model,
            RedirectAttributes ra) {

        Optional<Pedido> pedido = repoPed.findById(pedidoId);

        // Si el pedido no existe no podemos guardar el producto
        if (pedido.isEmpty()) {
            model.addAttribute("error", "El pedido con id " + pedidoId + " no existe");
            model.addAttribute("pedidos", repoPed.findAll());
            return "FormularioProducto";
        }

        // Añadimos el producto al pedido (asi el producto sabe a que pedido pertenece)
        pedido.get().addProducto(producto);

        // Guardamos el producto
        repoProd.save(producto);

        // para pasar datos a otro endpoint del controlador usamos redirectAttributes en
        // lugar de Model
        ra.addFlashAttribute("success", "El producto " + producto.getNombre() + " ha sido insertado correctamente");
        return "redirect:/producto/lista";
    }

}

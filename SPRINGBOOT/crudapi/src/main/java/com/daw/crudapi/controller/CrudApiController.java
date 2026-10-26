package com.daw.crudapi.controller;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import com.daw.crudapi.model.Cliente;
import com.daw.crudapi.model.Direccion;
import com.daw.crudapi.repository.ClienteRepository;
import com.daw.crudapi.repository.DireccionRepository;
import com.daw.crudapi.repository.PedidoRepository;

import net.datafaker.Faker;

@RestController // Las respuestas se devuelven como JSON
@RequestMapping("/crudapi") // Prefijo común de todas las URL
public class CrudApiController {

    @Autowired
    ClienteRepository repoCli;
    @Autowired
    DireccionRepository repoDir;
    @Autowired
    PedidoRepository repoPed;

    private final Faker faker = new Faker(Locale.forLanguageTag("es-ES"));

    @GetMapping
    public String index() {
        Cliente cliente = new Cliente();
        cliente.setNombre(faker.name().firstName());
        cliente.setApellidos(faker.name().lastName());
        cliente.setEmail(faker.internet().emailAddress());
        cliente.setTelefono(faker.phoneNumber().cellPhone());

        // Creamos una direccion
        Direccion direccion = new Direccion(null, faker.address().streetAddress(),
                Integer.toString(faker.number().numberBetween(1, 100)), faker.address().city(),
                faker.address().zipCode(), null);

        cliente.addDireccion(direccion);

        // Guardamos el cliente en bd con save
        // Esto hace un insert en bd
        // Devuelve una entity que representa al registro guardado en bd
        Cliente clienteGuardado = repoCli.save(cliente);
        // Guardamos tambien la dirección
        repoDir.save(direccion);

        // Si modificamos los datos del cliente y hacemos save otra vez
        // En este caso hace un update ya que la entity esta mapeada en bd
        // clienteGuardado.setApellidos("Gonsdfszalez Savedra");
        // repoCli.save(clienteGuardado);

        // Carga de entidades por id
        cliente = repoCli.findById(1L).orElse(null);

        return clienteGuardado.toString();
    }

    @GetMapping("/testconsultas")
    public String testConsultas() {

        List<Cliente> listaClientes;

        //Primero definimos el pageable con la pagina y cantidad de elementos por pagina, ordenado
        Pageable paginacion = PageRequest.of(1,40,Sort.by(Sort.Order.asc("nombre"),Sort.Order.desc("email")));

        //Despues usamos findall con esa paginacion para devolver un objeto page
        //Que tiene todos las entidades y los datos necesarios para la paginacion
        Page<Cliente> pagina = repoCli.findAll(paginacion);

        //Para ordenar una consulta usamos Sort.by, podemos meter varios campos de ordenacion
        listaClientes = repoCli.findAll(Sort.by("nombre").descending());

        //Borrar al cliente 3
        if (repoCli.existsById(3L))
            repoCli.deleteById(3L);

        //Con getContent sacamos la lista de clientes completa
        listaClientes = pagina.getContent();

        Optional<Cliente> cliente = repoCli.findByEmail("cliente.d8e4d76b@villalpando.org");

        listaClientes = repoCli.findTop8By(Sort.by("edad").ascending());

        boolean existenMenores = repoCli.existsByEdadLessThanEqual(5);

        return existenMenores?"Existen Menores":"No existen menores";

    }



}
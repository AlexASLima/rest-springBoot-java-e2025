package br.com.alex.controllers;

import br.com.alex.services.PersonServices;
import br.com.alex.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonServices service;
    // private PersonServices service = new PersonServices();

    //@RequestMapping(method = RequestMethod.GET, abaixo substitui esse q é legado
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<Person> findAll() {
        return service.findAll();
    }

    //@RequestMapping(value = "/{id}",
            //method = RequestMethod.GET,
    @GetMapping(value = "/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Person findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    //@RequestMapping(
            //method = RequestMethod.POST,
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Person create(@RequestBody Person person) {
        return service.create(person);
    }

    //@RequestMapping(
            //method = RequestMethod.PUT,
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Person update(@RequestBody Person person) {
        return service.update(person);
    }

    /* @RequestMapping(value = "/{id}",
            method = RequestMethod.DELETE
    ) */
    @DeleteMapping(value = "/{id}")
    /*public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    } abaixo alteração para retornar a resposta 204 (correto) ao deletar */
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package br.com.alex.controllers;

import br.com.alex.data.dto.v1.PersonDTO;
import br.com.alex.data.dto.v2.PersonDTOv2;
import br.com.alex.services.PersonServices;
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
    public List<PersonDTO> findAll() {
        return service.findAll();
    }

    //@RequestMapping(value = "/{id}",
            //method = RequestMethod.GET,
    @GetMapping(value = "/{id}",
                produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    //@RequestMapping(
            //method = RequestMethod.POST,
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO create(@RequestBody PersonDTO person) {
        return service.create(person);
    }
    @PostMapping(value = "/v2",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTOv2 createV2(@RequestBody PersonDTOv2 person) {
        return service.createV2(person);
    }

    //@RequestMapping(
            //method = RequestMethod.PUT,
    @PutMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO update(@RequestBody PersonDTO person) {
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

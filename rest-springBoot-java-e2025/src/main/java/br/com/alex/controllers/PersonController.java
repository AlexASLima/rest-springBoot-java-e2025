package br.com.alex.controllers;

import br.com.alex.data.dto.PersonDTO;
import br.com.alex.services.PersonServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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
        var person = service.findById(id);
        person.setBirthDay(new Date()); // mocado format date pois sem esse campo no BD
        person.setPhoneNumber("35 98803"); //       ||
        // person.setLastName(null); moc de teste de reiderizacao
        // person.setPhoneNumber("");           ||
        person.setSensitiveData("For bar");
        return person;
    }

    //@RequestMapping(
            //method = RequestMethod.POST,
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonDTO create(@RequestBody PersonDTO person) { return service.create(person); }

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

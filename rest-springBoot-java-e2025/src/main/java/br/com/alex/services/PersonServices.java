package br.com.alex.services;

import br.com.alex.controllers.PersonController;
import br.com.alex.exception.RequiredObjectIsNullException;
import br.com.alex.exception.ResourceNotFoundException;
// import br.com.alex.mapper.ObjectMapper;
import static br.com.alex.mapper.ObjectMapper.parseListObjects;
import static br.com.alex.mapper.ObjectMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.alex.model.Person;
import br.com.alex.data.dto.PersonDTO;
import br.com.alex.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.List;

@Service
public class PersonServices {
    // private final AtomicLong counter = new AtomicLong(); retirado para o testes mockito
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;
    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;

    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {
        logger.info("Finding all People!");

        var people = repository.findAll(pageable);
        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        String.valueOf(pageable.getSort())))
                .withSelfRel();

        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    public PagedModel<EntityModel<PersonDTO>> findByName(String firstName, Pageable pageable) {

        logger.info("Finding People by name!");

        var people = repository.findPeopleByName(firstName, pageable);

        var peopleWithLinks = people.map(person -> {
            var dto = parseObject(person, PersonDTO.class);
            addHateoasLinks(dto);
            return dto;
        });

        Link findAllLink = WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(PersonController.class)
                                .findAll(
                                        pageable.getPageNumber(),
                                        pageable.getPageSize(),
                                        String.valueOf(pageable.getSort())))
                .withSelfRel();
        return assembler.toModel(peopleWithLinks, findAllLink);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one PersonDTO!");

        var entity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No record foound this Id"));
        var dto =  parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public PersonDTO create(PersonDTO person) {
        if (person == null) throw  new RequiredObjectIsNullException();

        logger.info("Creating one Person!");

        var entity = parseObject(person, Person.class);
        var dto = parseObject(repository.save(entity), PersonDTO.class);

        addHateoasLinks(dto);
        return dto;
    }

    public PersonDTO update(PersonDTO person) {
        if (person == null) throw  new RequiredObjectIsNullException();

        logger.info("Updating one Person!");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records foound this Id"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var dto = parseObject(repository.save(entity), PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {
        logger.info("Disabling one Person!");

        repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.disablePerson(id);

        var entity = repository.findById(id).get();
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);

        return dto;
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records foound this Id"));

        repository.delete(entity);
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(1, 12, "asc")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}

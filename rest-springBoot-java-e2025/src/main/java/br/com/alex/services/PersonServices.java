package br.com.alex.services;

import br.com.alex.exception.ResourceNotFoundException;
// import br.com.alex.mapper.ObjectMapper;
import static br.com.alex.mapper.ObjectMapper.parseListObjects;
import static br.com.alex.mapper.ObjectMapper.parseObject;

import br.com.alex.model.Person;
import br.com.alex.data.dto.PersonDTO;
import br.com.alex.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;
import java.util.List;

@Service
public class PersonServices {
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    public List<PersonDTO> findAll() {
        logger.info("Finding all People!");

        // return ObjectMapper.parseListObjects(repository.findAll(), PersonDTO.class);
        // como o método usado acima é estatico, pode-se importar como statico e usar diretamente como abaixo. Veja acima como é o import.
        return parseListObjects(repository.findAll(), PersonDTO.class);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one PersonDTO!");

        var entity = repository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("No record foound this Id"));

        return parseObject(entity, PersonDTO.class);
    }

    public PersonDTO create(PersonDTO person) {
        logger.info("Creating one Person!");

        var entity = parseObject(person, Person.class);

        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public PersonDTO update(PersonDTO person) {
        logger.info("Updating one Person!");

        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records foound this Id"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return parseObject(repository.save(entity), PersonDTO.class);
    }

    public void delete(Long id) {
        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records foound this Id"));

        repository.delete(entity);
    }
}

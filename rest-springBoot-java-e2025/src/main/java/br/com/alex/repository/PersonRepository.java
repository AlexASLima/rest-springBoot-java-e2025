package br.com.alex.repository;

import br.com.alex.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // Assim já pode-se ter acesso a todos os métodos de um CRUD.
}

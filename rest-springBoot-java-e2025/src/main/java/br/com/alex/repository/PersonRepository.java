package br.com.alex.repository;

import br.com.alex.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // Assim já pode-se ter acesso a todos os métodos de um CRUD.

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Person p SET p.enabled = false WHERE p.id =:id") // Person, considerar como esta no objeto java e não como esta no BD.
    void disablePerson(@Param("id") Long id);
}

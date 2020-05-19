package com.minhabiblio.minhabiblioteca.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minhabiblio.minhabiblioteca.domain.model.Autor;

@Repository
public interface AutorRepository extends JpaRepository<Autor, Long>{

	Autor findByNome(String nome);
    
}

package com.minhabiblio.minhabiblioteca.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minhabiblio.minhabiblioteca.domain.model.Editora;

@Repository
public interface EditoraRepository extends JpaRepository<Editora, Long> {

	Editora findByNome(String nome);
    	
}

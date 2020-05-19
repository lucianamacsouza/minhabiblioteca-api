package com.minhabiblio.minhabiblioteca.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minhabiblio.minhabiblioteca.domain.model.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Long>{

	Genero findByNome(String nome);
	
}

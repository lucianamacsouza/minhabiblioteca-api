package com.minhabiblio.minhabiblioteca.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.minhabiblio.minhabiblioteca.domain.model.Genero;
import com.minhabiblio.minhabiblioteca.domain.model.Livro;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long>{

	Livro findByIsbn10(String isbn10);
	
	List<Livro> findByProprietarioId(Long proprietarioId);

	List<Livro> findByTituloContaining(String titulo);
	
}

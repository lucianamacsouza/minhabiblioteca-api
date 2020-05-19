package com.minhabiblio.minhabiblioteca.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.minhabiblio.minhabiblioteca.domain.model.Proprietario;

public interface ProprietarioRepository extends JpaRepository<Proprietario, Long>{

	Proprietario findByEmail(String email);
	
	List<Proprietario> findByNomeContaining(String nome);

}

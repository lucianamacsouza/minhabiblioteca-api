package com.minhabiblio.minhabiblioteca.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeEmUsoException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeJaExistenteException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeNaoEncontradaException;
import com.minhabiblio.minhabiblioteca.domain.model.Autor;
import com.minhabiblio.minhabiblioteca.domain.repository.AutorRepository;

@Service
public class CadastroAutorService {
	
	@Autowired
	private AutorRepository autorRepository;

	public Autor salvar( Autor autor ) {

		Autor autorExistente = autorRepository.findByNome(autor.getNome());
		
		if ( autorExistente != null && !autorExistente.equals(autor) ) {
			throw new EntidadeJaExistenteException("Já existe um autor com este nome");
		}
		
		return autorRepository.save(autor);		
	}
	
	public void excluir( Long autorId ) {
		try {
			
			autorRepository.deleteById(autorId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("O autor de código %d não foi encontrado", autorId));
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("O autor de código %d não pode ser removido, pois está em uso", autorId));
		}	
	}	
	

}

package com.minhabiblio.minhabiblioteca.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeEmUsoException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeJaExistenteException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeNaoEncontradaException;
import com.minhabiblio.minhabiblioteca.domain.model.Genero;
import com.minhabiblio.minhabiblioteca.domain.repository.GeneroRepository;

@Service
public class CadastroGeneroService {

	@Autowired
	private GeneroRepository generoRepository;

	public Genero salvar( Genero genero ) {

		Genero generoExistente = generoRepository.findByNome(genero.getNome());
		
		if ( generoExistente != null && !generoExistente.equals(genero) ) {
			throw new EntidadeJaExistenteException("Já existe um gênero cadastrado com este nome");
		}
		
		return generoRepository.save(genero);		
	}
	
	public void excluir( Long generoId ) {
		try {
			
			generoRepository.deleteById(generoId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("O gênero de código %d não foi encontrado", generoId));
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("O gênero de código %d não pode ser removido, pois está em uso", generoId));
		}	
	}	
}

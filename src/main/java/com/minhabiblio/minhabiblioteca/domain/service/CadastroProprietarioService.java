package com.minhabiblio.minhabiblioteca.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeEmUsoException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeNaoEncontradaException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeJaExistenteException;
import com.minhabiblio.minhabiblioteca.domain.model.Proprietario;
import com.minhabiblio.minhabiblioteca.domain.repository.ProprietarioRepository;

@Service
public class CadastroProprietarioService {
	
	@Autowired
	private ProprietarioRepository proprietarioRepository;

	public Proprietario salvar( Proprietario proprietario ) {

		Proprietario proprietarioExistente = proprietarioRepository.findByEmail(proprietario.getEmail());
		
		if ( proprietarioExistente != null && !proprietarioExistente.equals(proprietario) ) {
			throw new EntidadeJaExistenteException("Já existe um proprietário com este e-mail");
		}
		
		return proprietarioRepository.save(proprietario);		
	}
	
	
	public void excluir( Long proprietarioId ) {
		try {
			
			proprietarioRepository.deleteById(proprietarioId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("O proprietário de código %d não foi encontrado", proprietarioId));
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("O proprietário de código %d não pode ser removido, pois está em uso", proprietarioId));
		}			
	}	
		

}

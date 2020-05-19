package com.minhabiblio.minhabiblioteca.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeEmUsoException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeJaExistenteException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeNaoEncontradaException;
import com.minhabiblio.minhabiblioteca.domain.model.Editora;
import com.minhabiblio.minhabiblioteca.domain.repository.EditoraRepository;

@Service
public class CadastroEditoraService {

	@Autowired
	private EditoraRepository editoraRepository;

	public Editora salvar( Editora editora ) {

		Editora editoraExistente = editoraRepository.findByNome(editora.getNome());
		
		if ( editoraExistente != null && !editoraExistente.equals(editora) ) {
			throw new EntidadeJaExistenteException("Já existe uma editora com este nome");
		}
		
		return editoraRepository.save(editora);		
	}
	
	public void excluir( Long editoraId ) {
		try {
			
			editoraRepository.deleteById(editoraId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("A editora de código %d não foi encontrada", editoraId));
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("A editora de código %d não pode ser removida, pois está em uso", editoraId));
		}	
	}	
	
}

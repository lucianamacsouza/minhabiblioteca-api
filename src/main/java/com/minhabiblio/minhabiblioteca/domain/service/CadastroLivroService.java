package com.minhabiblio.minhabiblioteca.domain.service;

import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import com.minhabiblio.minhabiblioteca.domain.model.Autor;
import com.minhabiblio.minhabiblioteca.domain.model.Editora;
import com.minhabiblio.minhabiblioteca.domain.model.Genero;
import com.minhabiblio.minhabiblioteca.domain.model.Livro;
import com.minhabiblio.minhabiblioteca.domain.model.Proprietario;
import com.minhabiblio.minhabiblioteca.domain.repository.AutorRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.EditoraRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.GeneroRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.LivroRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.ProprietarioRepository;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeEmUsoException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeJaExistenteException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeNaoEncontradaException;

@Repository
public class CadastroLivroService {
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private EditoraRepository editoraRepository;
	
	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private ProprietarioRepository proprietarioRepository;

	public Livro salvar( Livro livro ) {
		
		Livro livroExistente = livroRepository.findByIsbn10(livro.getIsbn10());
		
		if ( livroExistente != null && !livroExistente.equals(livro) ) {
			throw new EntidadeJaExistenteException("Já existe um livro com este ISBN");
		}
		
		Autor autor = autorRepository.findById(livro.getAutor().getId())
				.orElseThrow( () -> new EntidadeNaoEncontradaException("Autor não encontrado") );
		
		Editora editora = editoraRepository.findById(livro.getEditora().getId())
				.orElseThrow( () -> new EntidadeNaoEncontradaException("Editora não encontrada") );			
		
		Genero genero = generoRepository.findById(livro.getGenero().getId())
				.orElseThrow( () -> new EntidadeNaoEncontradaException("Gênero não encontrado") );	
		
		Proprietario proprietario = proprietarioRepository.findById(livro.getProprietario().getId())
				.orElseThrow( () -> new EntidadeNaoEncontradaException("Proprietário não encontrado") );			
				
		livro.setAutor(autor);
		livro.setEditora(editora);
		livro.setGenero(genero);
		livro.setProprietario(proprietario);
		livro.setDataInclusao(OffsetDateTime.now());
		
		return livroRepository.save(livro);
		
	}	
	
	
	public void excluir( Long livroId ) {
		try {
			
			livroRepository.deleteById(livroId);
			
		} catch (EmptyResultDataAccessException e) {
			throw new EntidadeNaoEncontradaException(
					String.format("O livro de código %d não foi encontrado", livroId));
			
		} catch (DataIntegrityViolationException e) {
			throw new EntidadeEmUsoException(
					String.format("O livro de código %d não pode ser removido, pois está em uso", livroId));
		}			
	}
	
}

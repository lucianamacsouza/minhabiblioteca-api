package com.minhabiblio.minhabiblioteca.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.minhabiblio.minhabiblioteca.domain.model.Livro;
import com.minhabiblio.minhabiblioteca.domain.model.Proprietario;
import com.minhabiblio.minhabiblioteca.domain.repository.LivroRepository;
import com.minhabiblio.minhabiblioteca.domain.service.CadastroLivroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="Livros")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/livros")
public class LivroController {
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private CadastroLivroService cadastroLivro;

	@ApiOperation(value="Busca todos os livros cadastrados")
	@GetMapping
	public List<Livro> listar() {
		return livroRepository.findAll();
	}
	
	@ApiOperation(value="Busca um único livro de acordo com o seu Id")
	@GetMapping(value = "/{livroId}")
	public ResponseEntity<Livro> buscarPorId(@PathVariable("livroId") Long id ) {
		Optional<Livro> livro = livroRepository.findById(id);
		
		if ( livro.isPresent() ) {
			return ResponseEntity.ok(livro.get());
		}
		
		return ResponseEntity.notFound().build();
	}	

	@ApiOperation(value="Busca os livros cadastrados de acordo com o título ou parte dele")	
	@GetMapping(value = "/titulo/{tituloLivro}")
	public ResponseEntity<List<Livro>> listarPorTitulo(@PathVariable String tituloLivro) {
		
		List<Livro> livros = livroRepository.findByTituloContaining(tituloLivro);
		
		if ( livros != null ) {
			return ResponseEntity.status(HttpStatus.OK).body(livros);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	
	@ApiOperation(value="Adiciona um livro para um determinado proprietário")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Livro adicionar( @Valid @RequestBody Livro livro ) {
		return cadastroLivro.salvar(livro);
	}	
	
	@ApiOperation(value="Atualiza os dados de um livro")
	@PutMapping("/{livroId}")
	public ResponseEntity<Livro> atualizar(@Valid @PathVariable Long livroId, @RequestBody Livro livro ) {
		if ( !livroRepository.existsById(livroId) ) {
			return ResponseEntity.notFound().build();
		}
		
		livro.setId(livroId);
		livro = cadastroLivro.salvar(livro);
		
		return ResponseEntity.ok(livro);		
	}
		
	@ApiOperation(value="Exclui um livro de acordo com o seu Id")
	@DeleteMapping("/{livroId}")
	public ResponseEntity<Void> remover(@PathVariable Long livroId ) {
		if ( !livroRepository.existsById(livroId) ) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroLivro.excluir(livroId);
		
		return ResponseEntity.noContent().build();		
	}	
	
}

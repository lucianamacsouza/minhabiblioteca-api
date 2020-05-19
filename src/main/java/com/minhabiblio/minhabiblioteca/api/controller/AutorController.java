package com.minhabiblio.minhabiblioteca.api.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import com.minhabiblio.minhabiblioteca.domain.model.Autor;
import com.minhabiblio.minhabiblioteca.domain.repository.AutorRepository;
import com.minhabiblio.minhabiblioteca.domain.service.CadastroAutorService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="Autores")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/autores", produces = MediaType.APPLICATION_JSON_VALUE )
public class AutorController {

	@Autowired
	private AutorRepository autorRepository;
	
	@Autowired
	private CadastroAutorService cadastroAutor;

	@ApiOperation(value="Busca todos os autores cadastrados")
	@GetMapping
	public List<Autor> listar() {
		return autorRepository.findAll();
	}
	
	@ApiOperation(value="Busca um único autor de acordo com o seu Id")	
	@GetMapping(value = "/{autorId}")
	public ResponseEntity<Autor> buscarPorId(@PathVariable("autorId") Long id ) {
		Optional<Autor> autor = autorRepository.findById(id);
		
		if ( autor.isPresent() ) {
			return ResponseEntity.ok(autor.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value="Adiciona um autor")	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Autor adicionar( @Valid @RequestBody Autor autor ) {
		return cadastroAutor.salvar(autor);
	}
	
	@ApiOperation(value="Atualiza os dados de um autor")	
	@PutMapping("/{autorId}")
	public ResponseEntity<Autor> atualizar(@Valid @PathVariable Long autorId, @RequestBody Autor autor ) {
		if ( !autorRepository.existsById(autorId) ) {
			return ResponseEntity.notFound().build();
		}
		
		autor.setId(autorId);
		autor = cadastroAutor.salvar(autor);
		
		return ResponseEntity.ok(autor);		
	}
	

	@ApiOperation(value="Exclui um autor de acordo com o seu Id")
	@DeleteMapping("/{autorId}")
	public ResponseEntity<Void> remover(@PathVariable Long autorId ) {
		if ( !autorRepository.existsById(autorId) ) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroAutor.excluir(autorId);
		
		return ResponseEntity.noContent().build();		
	}
	
}

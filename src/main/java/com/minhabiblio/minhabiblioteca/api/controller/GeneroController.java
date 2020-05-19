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

import com.minhabiblio.minhabiblioteca.domain.model.Genero;
import com.minhabiblio.minhabiblioteca.domain.repository.GeneroRepository;
import com.minhabiblio.minhabiblioteca.domain.service.CadastroGeneroService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="Gêneros")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/generos", produces = MediaType.APPLICATION_JSON_VALUE )
public class GeneroController {
	
	@Autowired
	private GeneroRepository generoRepository;
	
	@Autowired
	private CadastroGeneroService cadastroGenero;
	
	@ApiOperation(value="Busca todos os gêneros literários cadastrados")
	@GetMapping
	public List<Genero> listar() {
		return generoRepository.findAll();
	}
	
	@ApiOperation(value="Busca um único gênero literário de acordo com o seu Id")
	@GetMapping(value = "/{generoId}")
	public ResponseEntity<Genero> buscarPorId(@PathVariable("generoId") Long id ) {
		Optional<Genero> genero = generoRepository.findById(id);
		
		if ( genero.isPresent() ) {
			return ResponseEntity.ok(genero.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value="Adiciona um gênero literário")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Genero adicionar( @Valid @RequestBody Genero genero ) {
		return cadastroGenero.salvar(genero);
	}
	
	@ApiOperation(value="Atualiza os dados de um gênero literário")
	@PutMapping("/{generoId}")
	public ResponseEntity<Genero> atualizar(@Valid @PathVariable Long generoId, @RequestBody Genero genero ) {
		if ( !generoRepository.existsById(generoId) ) {
			return ResponseEntity.notFound().build();
		}
		
		genero.setId(generoId);
		genero = cadastroGenero.salvar(genero);
		
		return ResponseEntity.ok(genero);		
	}
	
	@ApiOperation(value="Exclui um gênero literário de acordo com o seu Id")
	@DeleteMapping("/{generoId}")
	public ResponseEntity<Void> remover(@PathVariable Long generoId ) {
		if ( !generoRepository.existsById(generoId) ) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroGenero.excluir(generoId);
		
		return ResponseEntity.noContent().build();		
	}

}

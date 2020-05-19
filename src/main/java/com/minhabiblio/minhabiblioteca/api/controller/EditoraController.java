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

import com.minhabiblio.minhabiblioteca.domain.model.Editora;
import com.minhabiblio.minhabiblioteca.domain.repository.EditoraRepository;
import com.minhabiblio.minhabiblioteca.domain.service.CadastroEditoraService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="Editoras")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/editoras", produces = MediaType.APPLICATION_JSON_VALUE )
public class EditoraController {

	@Autowired
	private EditoraRepository editoraRepository;
	
	@Autowired
	private CadastroEditoraService cadastroEditora;

	@ApiOperation(value="Busca todas as editoras cadastradas")
	@GetMapping
	public List<Editora> listar() {
		return editoraRepository.findAll();
	}
	
	@ApiOperation(value="Busca uma única editora de acordo com o seu Id")		
	@GetMapping(value = "/{editoraId}")
	public ResponseEntity<Editora> buscarPorId(@PathVariable("editoraId") Long id ) {
		Optional<Editora> editora = editoraRepository.findById(id);
		
		if ( editora.isPresent() ) {
			return ResponseEntity.ok(editora.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value="Adiciona uma editora")
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Editora adicionar( @Valid @RequestBody Editora editora ) {
		return cadastroEditora.salvar(editora);
	}
	
	@ApiOperation(value="Atualiza os dados de uma editora")	
	@PutMapping("/{editoraId}")
	public ResponseEntity<Editora> atualizar(@Valid @PathVariable Long editoraId, @RequestBody Editora editora ) {
		if ( !editoraRepository.existsById(editoraId) ) {
			return ResponseEntity.notFound().build();
		}
		
		editora.setId(editoraId);
		editora = cadastroEditora.salvar(editora);
		
		return ResponseEntity.ok(editora);		
	}
	

	@ApiOperation(value="Exclui uma editora de acordo com o seu Id")
	@DeleteMapping("/{editoraId}")
	public ResponseEntity<Void> remover(@PathVariable Long editoraId ) {
		if ( !editoraRepository.existsById(editoraId) ) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroEditora.excluir(editoraId);
		
		return ResponseEntity.noContent().build();		
	}
	
}

package com.minhabiblio.minhabiblioteca.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
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

import com.minhabiblio.minhabiblioteca.api.model.LivroModel;
import com.minhabiblio.minhabiblioteca.api.model.ProprietarioModel;
import com.minhabiblio.minhabiblioteca.domain.model.Livro;
import com.minhabiblio.minhabiblioteca.domain.model.Proprietario;
import com.minhabiblio.minhabiblioteca.domain.repository.LivroRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.ProprietarioRepository;
import com.minhabiblio.minhabiblioteca.domain.service.CadastroProprietarioService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags="Proprietários")
@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(value = "/proprietarios", produces = MediaType.APPLICATION_JSON_VALUE )
public class ProprietarioController {
	
	@Autowired
	private ProprietarioRepository proprietarioRepository;
	
	@Autowired
	private LivroRepository livroRepository;
	
	@Autowired
	private CadastroProprietarioService cadastroProprietario;

	@Autowired
	private ModelMapper modelMapper;
		
	@ApiOperation(value="Busca todos os proprietários cadastrados")
	@GetMapping
	public List<Proprietario> listar() {
		return proprietarioRepository.findAll();
	}	
	
	@ApiOperation(value="Busca um único proprietário de acordo com o seu Id")	
	@GetMapping(value = "/{proprietarioId}")
	public ResponseEntity<Proprietario> buscarPorId(@PathVariable("proprietarioId") Long id ) {
		Optional<Proprietario> proprietario = proprietarioRepository.findById(id);
		
		if ( proprietario.isPresent() ) {
			return ResponseEntity.ok(proprietario.get());
		}
		
		return ResponseEntity.notFound().build();
	}
	
	@ApiOperation(value="Busca os proprietários cadastrados de acordo com o nome ou parte dele")	
	@GetMapping("/nome/{nomeProprietario}")
	public ResponseEntity<List<Proprietario>> listarPorNome(@PathVariable String nomeProprietario) {
		
		List<Proprietario> proprietarios = proprietarioRepository.findByNomeContaining(nomeProprietario);
		
		if ( proprietarios != null ) {
			return ResponseEntity.status(HttpStatus.OK).body(proprietarios);
		}
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	}
	
	@ApiOperation(value="Busca um os dados de um proprietário e seus livros")		
	@GetMapping(value="/{proprietarioId}/livros")
	public ResponseEntity<ProprietarioModel> buscar( @PathVariable Long proprietarioId ) {
		
		Optional<Proprietario> proprietario = proprietarioRepository.findById(proprietarioId);
		
		if ( proprietario.isPresent() ) {
			
			ProprietarioModel proprietarioModel = toProprietarioModel(proprietario.get());
			
			List<LivroModel> livros = toCollectionLivroModel(livroRepository.findByProprietarioId(proprietarioId));
			if ( livros != null ) {
				proprietarioModel.setLivros(livros);
			}
			
			return ResponseEntity.ok(proprietarioModel);
		}
		
		return ResponseEntity.notFound().build();
	}	
	
	@ApiOperation(value="Adiciona um proprietário")	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Proprietario adicionar( @Valid @RequestBody Proprietario proprietario ) {
		return cadastroProprietario.salvar(proprietario);
	}
	
	@ApiOperation(value="Atualiza os dados de um proprietário")	
	@PutMapping("/{proprietarioId}")
	public ResponseEntity<Proprietario> atualizar(@Valid @PathVariable Long proprietarioId, @RequestBody Proprietario proprietario ) {
		if ( !proprietarioRepository.existsById(proprietarioId) ) {
			return ResponseEntity.notFound().build();
		}
		
		proprietario.setId(proprietarioId);
		proprietario = cadastroProprietario.salvar(proprietario);
		
		return ResponseEntity.ok(proprietario);		
	}
	

	@ApiOperation(value="Exclui um proprietário de acordo com o seu Id")
	@DeleteMapping("/{proprietarioId}")
	public ResponseEntity<Void> remover(@PathVariable Long proprietarioId ) {
		if ( !proprietarioRepository.existsById(proprietarioId) ) {
			return ResponseEntity.notFound().build();
		}
		
		cadastroProprietario.excluir(proprietarioId);
		
		return ResponseEntity.noContent().build();		
	}
	
	private ProprietarioModel toProprietarioModel( Proprietario proprietario ) {
		return modelMapper.map(proprietario, ProprietarioModel.class);
	}	
		
	private LivroModel toLivroModel( Livro livro ) {
		return modelMapper.map(livro, LivroModel.class);
	}	
	
	private List<LivroModel> toCollectionLivroModel( List<Livro> livros) {
		return livros.stream()
				.map( livro -> toLivroModel(livro))
				.collect(Collectors.toList());
	}	

}

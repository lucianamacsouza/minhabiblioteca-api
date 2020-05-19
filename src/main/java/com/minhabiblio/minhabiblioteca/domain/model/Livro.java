package com.minhabiblio.minhabiblioteca.domain.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.*;

import com.minhabiblio.minhabiblioteca.domain.ValidationGroups;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity( name="livro" )
@Data
@NoArgsConstructor
public class Livro {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Size(max = 150)		
	private String titulo;
	
	@Size(max = 150)		
	private String subtitulo;
	
	@Column(name="codigo_barras")
	@NotBlank
	@Size(max = 20)		
	private String codigoBarras;
	
	@Column(name="ano_edicao")
	@NotNull
	private Integer anoEdicao;
	
	@Column(name="numero_edicao")
	@NotNull
	private Integer numeroEdicao;
	
	private String isbn10;
	
	@Column(name="data_inclusao")
	//@NotNull	
	private OffsetDateTime dataInclusao;
	
	@NotBlank
	private String resumo;
	
	@Valid
	@ConvertGroup( from = Default.class, to = ValidationGroups.AutorId.class)
	@NotNull
	@ManyToOne
	private Autor autor;
	
	@Valid
	@ConvertGroup( from = Default.class, to = ValidationGroups.GeneroId.class)
	@NotNull
	@ManyToOne
	private Genero genero;
	
	@Valid
	@ConvertGroup( from = Default.class, to = ValidationGroups.EditoraId.class)
	@NotNull
	@ManyToOne
	private Editora editora;
	
	@Valid
	@ConvertGroup( from = Default.class, to = ValidationGroups.ProprietarioId.class)
	@NotNull
	@ManyToOne
	private Proprietario proprietario;	
}

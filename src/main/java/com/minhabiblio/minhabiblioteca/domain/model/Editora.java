package com.minhabiblio.minhabiblioteca.domain.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.minhabiblio.minhabiblioteca.domain.ValidationGroups;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity( name="editora" )
@Data
@NoArgsConstructor
public class Editora {
	
	@NotNull( groups = ValidationGroups.EditoraId.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@NotBlank
	@Size(max = 60)
	private String nome;
	
	@NotBlank
	@Size(max = 100)
	private String cidade;
	
	@NotBlank
	@Size(max = 2)
	private String estado;


}

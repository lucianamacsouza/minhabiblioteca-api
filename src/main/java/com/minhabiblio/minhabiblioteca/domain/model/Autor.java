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

@Entity( name="autor" )
@Data
@NoArgsConstructor
public class Autor {
	
	@NotNull( groups = ValidationGroups.AutorId.class)
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)		
	private Long id;
	
	@NotBlank
	@Size(max = 80)
	private String nome;
	
	@NotBlank
	@Size(max = 60)
	private String nacionalidade;
	
}

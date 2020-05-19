package com.minhabiblio.minhabiblioteca.domain.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity( name="proprietario" )
@Data
@NoArgsConstructor
public class Proprietario {	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	
	@NotBlank
	@Size(max = 80)	
	private String nome;
	
	@NotBlank
	@Size(max = 80)	
	private String email;
	

}

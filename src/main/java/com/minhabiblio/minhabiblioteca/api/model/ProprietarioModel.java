package com.minhabiblio.minhabiblioteca.api.model;

import java.util.List;

import lombok.Data;

@Data
public class ProprietarioModel {
	
	private Long id;
    private String nome;
    private String email;
    
    private List<LivroModel> livros;

}

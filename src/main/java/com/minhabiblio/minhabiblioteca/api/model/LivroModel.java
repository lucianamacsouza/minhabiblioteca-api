package com.minhabiblio.minhabiblioteca.api.model;

import lombok.Data;

@Data
public class LivroModel {
	
	private Long id;
	private String titulo;
	private String subtitulo;
	private Integer anoEdicao;
	private Integer numeroEdicao;
	private String resumo;
	
	private GeneroModel genero;
	private EditoraModel editora;
	private AutorModel autor;

}

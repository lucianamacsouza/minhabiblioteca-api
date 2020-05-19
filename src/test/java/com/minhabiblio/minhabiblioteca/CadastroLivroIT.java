package com.minhabiblio.minhabiblioteca;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.minhabiblio.minhabiblioteca.domain.model.Livro;
import com.minhabiblio.minhabiblioteca.domain.model.Autor;
import com.minhabiblio.minhabiblioteca.domain.model.Editora;
import com.minhabiblio.minhabiblioteca.domain.model.Genero;
import com.minhabiblio.minhabiblioteca.domain.model.Proprietario;
import com.minhabiblio.minhabiblioteca.domain.repository.AutorRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.EditoraRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.GeneroRepository;
import com.minhabiblio.minhabiblioteca.domain.repository.ProprietarioRepository;
import com.minhabiblio.minhabiblioteca.domain.service.CadastroLivroService;
import com.minhabiblio.minhabiblioteca.util.DatabaseCleaner;
import com.minhabiblio.minhabiblioteca.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroLivroIT {

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private ProprietarioRepository proprietarioRepository;
	@Autowired
	private AutorRepository autorRepository;
	@Autowired
	private GeneroRepository generoRepository;	
	@Autowired
	private EditoraRepository editoraRepository;	
	
	@Autowired
	private CadastroLivroService cadastroLivro;
	
	private String jsonCorretoProprietario1;
	private String jsonCorretoAutor1;
	private String jsonCorretoGenero1;
	private String jsonCorretoEditora1;
	private String jsonCorretoLivro1;
	private String jsonCorretoLivro2;

    private Proprietario proprietario1;
    private Autor autor1;
    private Genero genero1;
    private Editora editora1;
    private Livro livro1;
    private Livro livro2;
    private int quantidadeLivrosCadastrados;
    private static final int LIVRO_ID_INEXISTENTE = 100;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/livros";

		databaseCleaner.clearTables();
		carregaDadosJson();
		prepararDados();
	}	
	
	@Test
	public void deveRetornar200_QuandoListarLivros() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());		
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeLivros_QuandoConsultarLivros() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeLivrosCadastrados));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarLivro() {
		given()
			.body(jsonCorretoLivro2)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}	
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarLivroExistente() {
		given()
			.pathParam("livroId", livro1.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{livroId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("titulo", equalTo(livro1.getTitulo()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarLivroInexistente() {
		given()
			.pathParam("livroId", LIVRO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{livroId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarStatus204_QuandoRemoverLivroExistente() {
		given()
			.pathParam("livroId", livro1.getId())
			.accept(ContentType.JSON)
		.when()
			.delete("/{livroId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}	
	
	@Test
	public void deveRetornarStatus404_QuandoRemoverLivroInexistente() {
		given()
			.pathParam("livroId", LIVRO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.delete("/{livroId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
	
	
	@Test
	public void deveRetornarStatus200_QuandoAtualizarLivroExistente() {
		given()
		.body(livro2)
			.pathParam("livroId", livro1.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{livroId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}	
		
	@Test
	public void deveRetornarStatus404_QuandoAtualizarLivroInexistente() {
		given()
		.body(livro2)
			.pathParam("livroId", LIVRO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{livroId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
			
	private void carregaDadosJson() {
		jsonCorretoProprietario1 = ResourceUtils.getContentFromResource(
				"/json/proprietario-correto1.json");
		jsonCorretoAutor1 = ResourceUtils.getContentFromResource(
				"/json/autor-correto1.json");
		jsonCorretoGenero1 = ResourceUtils.getContentFromResource(
				"/json/genero-correto1.json");
		jsonCorretoEditora1 = ResourceUtils.getContentFromResource(
				"/json/editora-correto1.json");
		jsonCorretoLivro1 = ResourceUtils.getContentFromResource(
				"/json/livro-correto1.json");
		jsonCorretoLivro2 = ResourceUtils.getContentFromResource(
				"/json/livro-correto2.json");
		

		try {
			proprietario1 = new ObjectMapper().readValue(jsonCorretoProprietario1, Proprietario.class);
			autor1 = new ObjectMapper().readValue(jsonCorretoAutor1, Autor.class);
			genero1 = new ObjectMapper().readValue(jsonCorretoGenero1, Genero.class);
			editora1 = new ObjectMapper().readValue(jsonCorretoEditora1, Editora.class);
			livro1 = new ObjectMapper().readValue(jsonCorretoLivro1, Livro.class);
			livro2 = new ObjectMapper().readValue(jsonCorretoLivro2, Livro.class);
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void prepararDados() {
		proprietario1 = proprietarioRepository.save(proprietario1);
		autor1 = autorRepository.save(autor1);
		genero1 = generoRepository.save(genero1);
		editora1 = editoraRepository.save(editora1);
		
		livro1 = cadastroLivro.salvar(livro1);
		
		quantidadeLivrosCadastrados = 1;
	}

}


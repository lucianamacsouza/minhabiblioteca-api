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
import com.minhabiblio.minhabiblioteca.domain.model.Autor;
import com.minhabiblio.minhabiblioteca.domain.repository.AutorRepository;
import com.minhabiblio.minhabiblioteca.util.DatabaseCleaner;
import com.minhabiblio.minhabiblioteca.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroAutorIT {

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private AutorRepository autorRepository;
	
	private String jsonCorretoAutor1;
	private String jsonCorretoAutor2;
	private String jsonCorretoAutor3;
	private String jsonCorretoAutor4;	
    private Autor autor1;
    private Autor autor2;
    private Autor autor3;
    private Autor autor4;
    private int quantidadeAutoresCadastrados;
    private static final int AUTOR_ID_INEXISTENTE = 100;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/autores";

		databaseCleaner.clearTables();
		carregaDadosJson();
		prepararDados();
	}	
	
	@Test
	public void deveRetornar200_QuandoListarAutores() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());		
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeAutores_QuandoConsultarAutores() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeAutoresCadastrados));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarAutor() {
		given()
			.body(jsonCorretoAutor4)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}	
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarAutorExistente() {
		given()
			.pathParam("autorId", autor1.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{autorId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(autor1.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarAutorInexistente() {
		given()
			.pathParam("autorId", AUTOR_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{autorId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarStatus204_QuandoRemoverAutorExistente() {
		given()
			.pathParam("autorId", autor3.getId())
			.accept(ContentType.JSON)
		.when()
			.delete("/{autorId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}	
	
	@Test
	public void deveRetornarStatus404_QuandoRemoverAutorInexistente() {
		given()
			.pathParam("autorId", AUTOR_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.delete("/{autorId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
	
	
	@Test
	public void deveRetornarStatus200_QuandoAtualizarAutorExistente() {
		given()
     	    .body(autor4)
			.pathParam("autorId", autor1.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{autorId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}	
		
	@Test
	public void deveRetornarStatus404_QuandoAtualizarAutorInexistente() {
		given()
		.body(autor1)
			.pathParam("autorId", AUTOR_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{autorId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
			
	private void carregaDadosJson() {
		jsonCorretoAutor1 = ResourceUtils.getContentFromResource(
				"/json/autor-correto1.json");
		jsonCorretoAutor2 = ResourceUtils.getContentFromResource(
				"/json/autor-correto2.json");
		jsonCorretoAutor3 = ResourceUtils.getContentFromResource(
				"/json/autor-correto3.json");	
		jsonCorretoAutor4 = ResourceUtils.getContentFromResource(
				"/json/autor-correto4.json");			

		try {
			autor1 = new ObjectMapper().readValue(jsonCorretoAutor1, Autor.class);
			autor2 = new ObjectMapper().readValue(jsonCorretoAutor2, Autor.class);
			autor3 = new ObjectMapper().readValue(jsonCorretoAutor3, Autor.class);
			autor4 = new ObjectMapper().readValue(jsonCorretoAutor4, Autor.class);
			//System.out.println(genero1.toString());			
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void prepararDados() {
		autor1 = autorRepository.save(autor1);
		autor2 = autorRepository.save(autor2);
		autor3 = autorRepository.save(autor3);
		
		quantidadeAutoresCadastrados = (int) autorRepository.count();
	}

}


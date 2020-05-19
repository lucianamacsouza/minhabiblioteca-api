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
import com.minhabiblio.minhabiblioteca.domain.model.Genero;
import com.minhabiblio.minhabiblioteca.domain.repository.GeneroRepository;
import com.minhabiblio.minhabiblioteca.util.DatabaseCleaner;
import com.minhabiblio.minhabiblioteca.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroGeneroIT {

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private GeneroRepository generoRepository;
	
	private String jsonCorretoGenero1;
	private String jsonCorretoGenero2;
	private String jsonCorretoGenero3;
	private String jsonCorretoGenero4;	
    private Genero genero1;
    private Genero genero2;
    private Genero genero3;
    private Genero genero4;
    private int quantidadeGenerosCadastrados;
    private static final int GENERO_ID_INEXISTENTE = 100;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/generos";

		databaseCleaner.clearTables();
		carregaDadosJson();
		prepararDados();
	}	
	
	@Test
	public void deveRetornar200_QuandoListarGeneros() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());		
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeGeneros_QuandoConsultarGeneros() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeGenerosCadastrados));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarGenero() {
		given()
			.body(jsonCorretoGenero4)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}	
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarGeneroExistente() {
		given()
			.pathParam("generoId", genero1.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{generoId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(genero1.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarGeneroInexistente() {
		given()
			.pathParam("generoId", GENERO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{generoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarStatus204_QuandoRemoverGeneroExistente() {
		given()
			.pathParam("generoId", genero3.getId())
			.accept(ContentType.JSON)
		.when()
			.delete("/{generoId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}	
	
	@Test
	public void deveRetornarStatus404_QuandoRemoverGeneroInexistente() {
		given()
			.pathParam("generoId", GENERO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.delete("/{generoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
	
	
	@Test
	public void deveRetornarStatus200_QuandoAtualizarGeneroExistente() {
		given()
		.body(genero4)
			.pathParam("generoId", genero3.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{generoId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}	
		
	@Test
	public void deveRetornarStatus404_QuandoAtualizarGeneroInexistente() {
		given()
		.body(genero4)
			.pathParam("generoId", GENERO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{generoId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
			
	private void carregaDadosJson() {
		jsonCorretoGenero1 = ResourceUtils.getContentFromResource(
				"/json/genero-correto1.json");
		jsonCorretoGenero2 = ResourceUtils.getContentFromResource(
				"/json/genero-correto2.json");
		jsonCorretoGenero3 = ResourceUtils.getContentFromResource(
				"/json/genero-correto3.json");	
		jsonCorretoGenero4 = ResourceUtils.getContentFromResource(
				"/json/genero-correto4.json");			
		//System.out.println("Objeto : " + jsonCorretoGenero1);
		try {
			genero1 = new ObjectMapper().readValue(jsonCorretoGenero1, Genero.class);
			genero2 = new ObjectMapper().readValue(jsonCorretoGenero2, Genero.class);
			genero3 = new ObjectMapper().readValue(jsonCorretoGenero3, Genero.class);
			genero4 = new ObjectMapper().readValue(jsonCorretoGenero4, Genero.class);
			//System.out.println(genero1.toString());			
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void prepararDados() {
		genero1 = generoRepository.save(genero1);
		genero2 = generoRepository.save(genero2);
		genero3 = generoRepository.save(genero3);
		
		quantidadeGenerosCadastrados = (int) generoRepository.count();
	}

}

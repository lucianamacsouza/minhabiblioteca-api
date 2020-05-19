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
import com.minhabiblio.minhabiblioteca.domain.model.Proprietario;
import com.minhabiblio.minhabiblioteca.domain.repository.ProprietarioRepository;
import com.minhabiblio.minhabiblioteca.util.DatabaseCleaner;
import com.minhabiblio.minhabiblioteca.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroProprietarioIT {

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private ProprietarioRepository proprietarioRepository;
	
	private String jsonCorretoProprietario1;
	private String jsonCorretoProprietario2;
	private String jsonCorretoProprietario3;
	private String jsonCorretoProprietario4;	
    private Proprietario proprietario1;
    private Proprietario proprietario2;
    private Proprietario proprietario3;
    private Proprietario proprietario4;
    private int quantidadeProprietariosCadastrados;
    private static final int PROPRIETARIO_ID_INEXISTENTE = 100;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/proprietarios";

		databaseCleaner.clearTables();
		carregaDadosJson();
		prepararDados();
	}	
	
	@Test
	public void deveRetornar200_QuandoListarProprietarios() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());		
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeProprietarios_QuandoConsultarProprietarios() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeProprietariosCadastrados));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarProprietario() {
		given()
			.body(jsonCorretoProprietario4)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}	
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarProprietarioExistente() {
		given()
			.pathParam("proprietarioId", proprietario1.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{proprietarioId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(proprietario1.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarProprietarioInexistente() {
		given()
			.pathParam("proprietarioId", PROPRIETARIO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{proprietarioId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarStatus204_QuandoRemoverProprietarioExistente() {
		given()
			.pathParam("proprietarioId", proprietario3.getId())
			.accept(ContentType.JSON)
		.when()
			.delete("/{proprietarioId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}	
	
	@Test
	public void deveRetornarStatus404_QuandoRemoverProprietarioInexistente() {
		given()
			.pathParam("proprietarioId", PROPRIETARIO_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.delete("/{proprietarioId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
	
	
	@Test
	public void deveRetornarStatus200_QuandoAtualizarProprietarioExistente() {
		given()
		.body(proprietario4)
			.pathParam("proprietarioId", proprietario3.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{proprietarioId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}	
		
	@Test
	public void deveRetornarStatus404_QuandoAtualizarProprietarioInexistente() {
		given()
		.body(proprietario4)
			.pathParam("proprietarioId", PROPRIETARIO_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{proprietarioId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
			
	private void carregaDadosJson() {
		jsonCorretoProprietario1 = ResourceUtils.getContentFromResource(
				"/json/proprietario-correto1.json");
		jsonCorretoProprietario2 = ResourceUtils.getContentFromResource(
				"/json/proprietario-correto2.json");
		jsonCorretoProprietario3 = ResourceUtils.getContentFromResource(
				"/json/proprietario-correto3.json");	
		jsonCorretoProprietario4 = ResourceUtils.getContentFromResource(
				"/json/proprietario-correto4.json");			

		try {
			proprietario1 = new ObjectMapper().readValue(jsonCorretoProprietario1, Proprietario.class);
			proprietario2 = new ObjectMapper().readValue(jsonCorretoProprietario2, Proprietario.class);
			proprietario3 = new ObjectMapper().readValue(jsonCorretoProprietario3, Proprietario.class);
			proprietario4 = new ObjectMapper().readValue(jsonCorretoProprietario4, Proprietario.class);			
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void prepararDados() {
		proprietario1 = proprietarioRepository.save(proprietario1);
		proprietario2 = proprietarioRepository.save(proprietario2);
		proprietario3 = proprietarioRepository.save(proprietario3);
		
		quantidadeProprietariosCadastrados = (int) proprietarioRepository.count();
	}

}


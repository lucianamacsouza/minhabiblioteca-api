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
import com.minhabiblio.minhabiblioteca.domain.model.Editora;
import com.minhabiblio.minhabiblioteca.domain.repository.EditoraRepository;
import com.minhabiblio.minhabiblioteca.util.DatabaseCleaner;
import com.minhabiblio.minhabiblioteca.util.ResourceUtils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("/application-test.properties")
class CadastroEditoraIT {

	@LocalServerPort
	private int port;
	
	@Autowired
	private DatabaseCleaner databaseCleaner;
	
	@Autowired
	private EditoraRepository editoraRepository;
	
	private String jsonCorretoEditora1;
	private String jsonCorretoEditora2;
	private String jsonCorretoEditora3;
	private String jsonCorretoEditora4;	
    private Editora editora1;
    private Editora editora2;
    private Editora editora3;
    private Editora editora4;
    private int quantidadeEditorasCadastradas;
    private static final int EDITORA_ID_INEXISTENTE = 100;
	
	@BeforeEach
	public void setUp() {
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.port = port;
		RestAssured.basePath = "/editoras";

		databaseCleaner.clearTables();
		carregaDadosJson();
		prepararDados();
	}	
	
	@Test
	public void deveRetornar200_QuandoListarEditoras() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.statusCode(HttpStatus.OK.value());		
	}
	
	@Test
	public void deveRetornarQuantidadeCorretaDeEditoras_QuandoConsultarEditoras() {
		given()
			.accept(ContentType.JSON)
		.when()
			.get()
		.then()
			.body("", hasSize(quantidadeEditorasCadastradas));
	}
	
	@Test
	public void deveRetornarStatus201_QuandoCadastrarEditora() {
		given()
			.body(jsonCorretoEditora4)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.post()
		.then()
			.statusCode(HttpStatus.CREATED.value());
	}	
	
	@Test
	public void deveRetornarRespostaEStatusCorretos_QuandoConsultarEditoraExistente() {
		given()
			.pathParam("editoraId", editora1.getId())
			.accept(ContentType.JSON)
		.when()
			.get("/{editoraId}")
		.then()
			.statusCode(HttpStatus.OK.value())
			.body("nome", equalTo(editora1.getNome()));
	}
	
	@Test
	public void deveRetornarStatus404_QuandoConsultarEditoraInexistente() {
		given()
			.pathParam("editoraId", EDITORA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.get("/{editoraId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
	@Test
	public void deveRetornarStatus204_QuandoRemoverEditoraExistente() {
		given()
			.pathParam("editoraId", editora3.getId())
			.accept(ContentType.JSON)
		.when()
			.delete("/{editoraId}")
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}	
	
	@Test
	public void deveRetornarStatus404_QuandoRemoverEditoraInexistente() {
		given()
			.pathParam("editoraId", EDITORA_ID_INEXISTENTE)
			.accept(ContentType.JSON)
		.when()
			.delete("/{editoraId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
	
	
	@Test
	public void deveRetornarStatus200_QuandoAtualizarEditoraExistente() {
		given()
		.body(editora4)
			.pathParam("editoraId", editora3.getId())
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{editoraId}")
		.then()
			.statusCode(HttpStatus.OK.value());
	}	
		
	@Test
	public void deveRetornarStatus404_QuandoAtualizarEditorarInexistente() {
		given()
		.body(editora4)
			.pathParam("editoraId", EDITORA_ID_INEXISTENTE)
			.contentType(ContentType.JSON)
			.accept(ContentType.JSON)
		.when()
			.put("/{editoraId}")
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}	
			
	private void carregaDadosJson() {
		jsonCorretoEditora1 = ResourceUtils.getContentFromResource(
				"/json/editora-correto1.json");
		jsonCorretoEditora2 = ResourceUtils.getContentFromResource(
				"/json/editora-correto2.json");
		jsonCorretoEditora3 = ResourceUtils.getContentFromResource(
				"/json/editora-correto3.json");	
		jsonCorretoEditora4 = ResourceUtils.getContentFromResource(
				"/json/editora-correto4.json");			

		try {
			editora1 = new ObjectMapper().readValue(jsonCorretoEditora1, Editora.class);
			editora2 = new ObjectMapper().readValue(jsonCorretoEditora2, Editora.class);
			editora3 = new ObjectMapper().readValue(jsonCorretoEditora3, Editora.class);
			editora4 = new ObjectMapper().readValue(jsonCorretoEditora4, Editora.class);
			//System.out.println(genero1.toString());			
			
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void prepararDados() {
		editora1 = editoraRepository.save(editora1);
		editora2 = editoraRepository.save(editora2);
		editora3 = editoraRepository.save(editora3);
		
		quantidadeEditorasCadastradas = (int) editoraRepository.count();
	}

}

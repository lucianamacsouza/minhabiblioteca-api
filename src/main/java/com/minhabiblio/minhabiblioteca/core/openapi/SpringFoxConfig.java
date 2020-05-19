package com.minhabiblio.minhabiblioteca.core.openapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer{
	
	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.minhabiblio.minhabiblioteca"))
				.build()
				.apiInfo(apiInfo())
				.tags(new Tag("Livros", "Gerencimento do cadastro de livros de cada proprietário"),
					  new Tag("Gêneros","Gerenciamento do cadastro de gêneros literários"),
					  new Tag("Autores","Gerenciamento do cadastro de autores"),
					  new Tag("Editoras","Gerenciamento do cadastro de editoras"),
					  new Tag("Proprietários","Gerenciamento do cadastro de proprietários dos livros"));
	}

	public ApiInfo apiInfo() {
		return new ApiInfoBuilder()
				.title("Minha Biblioteca API - Exemplo")
				.description("API de exemplo para catálogo de livros que uma pessoa possui e possa vender ou emprestar")
				.version("v 1.0.0")
				.contact( new Contact("Luciana Carvalho de Souza", "http://www.lunasistemas.net/", "contato@lunasistemas.net"))
				.build();
	}
	
	@Override
	public void addResourceHandlers( ResourceHandlerRegistry registry ) {
		registry.addResourceHandler("swagger-ui.html")
			.addResourceLocations("classpath:/META-INF/resources/");
		
		registry.addResourceHandler("/webjars/**)")
			.addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
}

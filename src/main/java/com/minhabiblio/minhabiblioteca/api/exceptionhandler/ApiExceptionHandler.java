package com.minhabiblio.minhabiblioteca.api.exceptionhandler;

import java.time.OffsetDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeEmUsoException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeJaExistenteException;
import com.minhabiblio.minhabiblioteca.domain.exception.EntidadeNaoEncontradaException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		var campos = new ArrayList<ProblemaRetorno.Campo>();
		
		for ( ObjectError error : ex.getBindingResult().getAllErrors()) {
			String nome = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());
		
			campos.add(new ProblemaRetorno.Campo(nome, mensagem));
		}
		
		var problema = new ProblemaRetorno();
	    problema.setStatus(status.value());
	    problema.setTitulo("Um ou mais campos estão inválidos. "
	    		+ "Faça o preenchimento correto e tente novamente.");
	    problema.setDataHora(OffsetDateTime.now());
	    problema.setCampos(campos);
		
		return super.handleExceptionInternal(ex, problema, headers, status, request);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<Object> handleNegocio( EntidadeEmUsoException ex, WebRequest request ) {
		var status = HttpStatus.BAD_REQUEST;
		
		var problema = new ProblemaRetorno();
		problema.setStatus(status.value());
		problema.setDataHora(OffsetDateTime.now());
		problema.setTitulo(ex.getMessage());
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> handleEntidadeNaoEncontrada( EntidadeNaoEncontradaException ex, WebRequest request ) {
		var status = HttpStatus.NOT_FOUND;
		
		var problema = new ProblemaRetorno();
		problema.setStatus(status.value());
		problema.setDataHora(OffsetDateTime.now());
		problema.setTitulo(ex.getMessage());
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}	

	@ExceptionHandler(EntidadeJaExistenteException.class)
	public ResponseEntity<Object> handleEntidadeJaExistente( EntidadeJaExistenteException ex, WebRequest request ) {
		var status = HttpStatus.NOT_FOUND;
		
		var problema = new ProblemaRetorno();
		problema.setStatus(status.value());
		problema.setDataHora(OffsetDateTime.now());
		problema.setTitulo(ex.getMessage());
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), status, request);
	}
}

package com.xantrix.webapp.exception;

public class NotFoundException extends Exception{

	private static final long serialVersionUID = -8729169303699924451L;
	
	private String messaggio="Elemento non trovato";

	public NotFoundException() {
		super();
	}
	
	public NotFoundException(String msg) {
		super(msg);
		this.messaggio = msg;
	}

	public String getMessaggio() {
		return messaggio;
	}

	public void setMessaggio(String messaggio) {
		this.messaggio = messaggio;
	}
	
}

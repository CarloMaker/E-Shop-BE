package com.xantrix.webapp.exception;

public class DuplicateException extends Exception{

	private static final long serialVersionUID = -8729169303699924451L;
	
	private String messaggio;

	public DuplicateException() {
		super();
	}
	
	public DuplicateException(String msg) {
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

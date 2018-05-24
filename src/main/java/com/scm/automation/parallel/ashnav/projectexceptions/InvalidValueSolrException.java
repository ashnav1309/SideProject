package com.scm.automation.parallel.ashnav.projectexceptions;

public class InvalidValueSolrException extends Exception{

	private static final long serialVersionUID = 1L;
	String message;
	public InvalidValueSolrException(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return message;
	}
}

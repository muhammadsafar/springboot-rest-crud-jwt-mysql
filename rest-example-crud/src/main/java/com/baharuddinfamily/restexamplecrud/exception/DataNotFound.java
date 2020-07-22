package com.baharuddinfamily.restexamplecrud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DataNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataNotFound(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

}

package com.jsantos.orm.exceptions;

import com.jsantos.common.util.ListValues;

public class ConstraintsException extends ApiException{

	
	private static final long serialVersionUID = 1L;

	ListValues<IValidationError> errors=new ListValues<IValidationError>();
	
	public ConstraintsException(ApiError tollApiError, ListValues<IValidationError> errors) {
		super(tollApiError);
		this.errors = errors;
	}

	public ListValues<IValidationError> getErrors() {
		return errors;
	}
}

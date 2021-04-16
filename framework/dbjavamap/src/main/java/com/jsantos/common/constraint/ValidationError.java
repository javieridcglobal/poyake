package com.jsantos.common.constraint;

import com.jsantos.common.util.ListValues;
import com.jsantos.orm.exceptions.IValidationError;



public class ValidationError implements IValidationError{

	private String messageCode;
	private ListValues<Object> parameters=new ListValues<Object>();
	
	
	@Override
	public String getMessageCode() {
		return messageCode;
	}
	@Override
	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}
	@Override
	public ListValues<Object> getParameters() {
		return parameters;
	}
	@Override
	public void setParameters(ListValues<Object> parameters) {
		this.parameters = parameters;
	}
}

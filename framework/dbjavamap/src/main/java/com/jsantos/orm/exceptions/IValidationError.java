package com.jsantos.orm.exceptions;

import com.jsantos.common.util.ListValues;

public interface IValidationError {

	String getMessageCode();

	void setMessageCode(String messageCode);

	ListValues<Object> getParameters();

	void setParameters(ListValues<Object> parameters);

}
package com.jsantos.orm.exceptions;

import com.jsantos.common.util.MapValues;

public class ApiException extends RuntimeException{

	private static final long serialVersionUID = 8261606342471094743L;

	private ApiError error = ApiError.UNKNOWN_ERROR;
	private String body;
	private Object data;

	public ApiException(ApiError tollApiError) {
		this.error = tollApiError;
	}

	public ApiException(ApiError tollApiError, String message) {
		super(message);
		this.error = tollApiError;
	}

	public ApiException(ApiError tollApiError, Throwable cause) {
		super(cause);
		this.error = tollApiError;
	}

	public ApiException(ApiError tollApiError, String message, Throwable cause) {
		super(message, cause);
		this.error = tollApiError;
	}
			
	public MapValues<Object> apiErrorProperties() {
		MapValues<Object> properties = new MapValues<Object>();
		
		properties.put("body", body);
		properties.put("errorName", this.error.name());
		//properties.put("errorCode", this.error.getCode());
		//properties.put("errorCodeDescription", this.error.getCodeDescription());
		// TODO: disable this if it is not in dev mode:
		properties.put("errorMessage", this.getMessage());
		//properties.put("errorStackTrace", ExceptionUtils.exceptionStackTraceAsString(this));
		properties.put("errors", data);
		return properties;
	}

	public ApiError getError() {
		return error;
	}

	public void setError(ApiError error) {
		this.error = error;
	}

	
	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String getMessage() {
		return this.error.name();
	}

	public void setData(Object data) {
		this.data = data;
	}
	
	
}




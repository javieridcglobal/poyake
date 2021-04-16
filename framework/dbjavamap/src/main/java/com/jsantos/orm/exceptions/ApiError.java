package com.jsantos.orm.exceptions;

public enum ApiError {
	// @formatter:off
	/* Generic Errors */
	UNKNOWN_ERROR(0, "An unknown error has occurred."),  
	NOT_FOUND(1, "Resource not found"),  
	FILE_NOT_FOUND(3, "An error has occured due to file not found."),
	VALIDATION_ERROR(4,  "Validation error."),
	NULL_POINTER_EXCEPTION(5, "Null pointer exception."),
	RULE_NOT_FOUND(6, "Rule was not found."),
	PRE_COND_FAILED(7, "Pre Condition Failed"),
	POST_COND_FAILED(8, "Post Condition Failed"),
	NOT_IMPLEMENTED(9, "Not implemented."),
	NOT_UNIQUE(10, "Not unique."),
	IO_ERROR(11, "IO Error"),
	ZIP_CREATION_ERROR(12, "Could not created zip."),
	PARSING_ERROR(12, "Parsing Error"),
	FILE_UPLOAD_ERROR(13, "File upload Error"),
	FILE_DOWNLOAD_ERROR(14, "File Download Error"),
	SFTP_CONNECTION_ERROR(15, "SFTP Connection Error."),
	FILE_WRITE_ERROR(16, "File Write Error."),
	FILE_READ_ERROR(17, "File Read Error."),
	NOT_ALLOWED(18, "Operation not allowed"),
	BUSINESS_CONFIG_NOT_FOUND(19, "Business config not found"),
	CONSTRAINT_NOT_ACOMPLISH(20, "Constraint not Acomplish"),
	/* Database Errors */
	DB_BAD_SQL(100, "A database error has occured due to bad sql."), 
	DB_BAD_METADATA(101, "A database error has occured due to bad metadata."),
	DB_WRONG_DATA(102, "Wrong Data in Db"),
	DB_ERROR(103, "Data conflict occurred while processing the request"),
	DB_DUPLICATED_DATA(104, "Data duplication error"),

		/* TOLL-SECURITY Errors */
	SECURITY_USER_DISABLED(300, "User disabled"), 
	SECURITY_INVALID_EFFECTIVE_START_DATE(301, "Invalid Effective Start Date"),
	SECURITY_UNAUTHORIZED(302, "Wrong username or password."),
	SECURITY_FORBIDDEN(303,  "Access denied."),
	SECURITY_GENERAL_ERROR(306, "Security General Error"),
	
	
	JOB_ERROR(700, "Job error."),
	
	/* Custom Fields */
	CUSTOM_FIELDS_LIMIT_EXCEEDED_ERROR(900, "Custom field limit has already reached"),
	
	/*GL Errors*/
	GL_GENERAL_ERROR(1100, "GL Error"),
	
	/* Collection Errors */
	COLLECTION_GENERAL_ERROR(1200, "Collections Error");
	
	// @formatter:on
	
	private final int code;
	
	private final String codeDescription;
	
	private Object status;
	
	private ApiError(int code, String codeDescription) {
		this.code = code;
		
		this.codeDescription = codeDescription;
	}

	@Override
	public String toString() {
		return "Name = " + this.name() + "; Code = " + code + ";  CodeDescription = " + codeDescription;
	}

	public Object getStatus() {
		return status;
	}

	public void setStatus(Object status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public String getCodeDescription() {
		return codeDescription;
	}
}
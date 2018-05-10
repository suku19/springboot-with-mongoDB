/**
 * 
 */
package com.user.app.exception;

/**
 * @author Sukanta
 *
 */
public class UserException extends Exception{

	private static final long serialVersionUID = 1L;
	private String errorMessage;
	private String code;

	public UserException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}
	
	public UserException(String code,String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
		this.code=code;
	}

	public UserException() {
		super();
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
/**
 * 
 */
package com.user.app.exception;

/**
 * @author Sukanta
 *
 */
public class ErrorResponse {

	private boolean success;
	private String errorCode;
	private String message;


	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public boolean isSuccess() {
		return success;
	}
	
	public void setSuccess(boolean success) {
		this.success = success;
	}
}

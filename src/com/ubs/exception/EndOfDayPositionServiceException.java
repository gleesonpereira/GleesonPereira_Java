package com.ubs.exception;

public class EndOfDayPositionServiceException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3694738246245592022L;
	@SuppressWarnings("unused")
	private String message;
	@SuppressWarnings("unused")
	private Throwable exception;
	
	public EndOfDayPositionServiceException(Throwable exception) {
		super(exception);
		this.exception = exception;
	}
	
	public EndOfDayPositionServiceException(String message, Throwable exception) {
		super(message, exception);
		this.message = message;
		this.exception = exception;
	}
	
	public EndOfDayPositionServiceException(String message) {
		super(message);
		this.message = message;
	}
}

package com.capgemini.jdbc;

@SuppressWarnings("serial")
public class EmployeePayrollServiceJDBCException extends Exception{
	public enum ExceptionType {
		WRONG_CREDENTIALS, CANNOT_LOAD_DRIVER
	}

	ExceptionType type;

	public EmployeePayrollServiceJDBCException(String message, ExceptionType type) {
		super(message);
		this.type = type;
	}
}





package com.capgemini.jdbc;

@SuppressWarnings("serial")
public class EmployeePayrollJDBCException extends Exception{
	public enum ExceptionType {
		WRONG_CREDENTIALS, CANNOT_LOAD_DRIVER;
	}

	ExceptionType type;

	public EmployeePayrollJDBCException(String message, ExceptionType type) {
		super(message);
		this.type = type;
	}
}

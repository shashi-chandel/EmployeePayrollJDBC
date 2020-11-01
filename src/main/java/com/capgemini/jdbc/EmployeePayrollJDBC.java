package com.capgemini.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.capgemini.jdbc.EmployeePayrollJDBCException.ExceptionType;

public class EmployeePayrollJDBC {
	private static final Logger log = LogManager.getLogger(EmployeePayrollJDBC.class);

	private static final String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
	private static final String user = "root";
	private static final String password = "Shashi@123";

	public static void main(String[] args) throws EmployeePayrollJDBCException {
		EmployeePayrollJDBC employeePayrollJDBC = new EmployeePayrollJDBC();
		employeePayrollJDBC.establishConnection();
	}

	/**
	 * UC1 Connection to Database
	 * 
	 * @throws EmployeePayrollJDBCException
	 */
	private void establishConnection() throws EmployeePayrollJDBCException {
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			log.info("Driver found!");
		} catch (ClassNotFoundException e) {
			throw new EmployeePayrollJDBCException("Cannot find the JDBC Driver!!", ExceptionType.CANNOT_LOAD_DRIVER);
		}
		listDrivers();
		try {
			log.info("\nConnecting to database: " + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, user, password);
			log.info("Connection estabilshed with: " + connection);
		} catch (SQLException e) {
			throw new EmployeePayrollJDBCException("Cannot connect to the JDBC Driver!!",
					ExceptionType.WRONG_CREDENTIALS);
		}
	}

	private void listDrivers() {
		Enumeration<Driver> driverList = DriverManager.getDrivers();
		while (driverList.hasMoreElements()) {
			Driver driverClass = driverList.nextElement();
			log.info("Driver: " + driverClass.getClass().getName());
		}
	}
}
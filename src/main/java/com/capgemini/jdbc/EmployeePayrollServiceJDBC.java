package com.capgemini.jdbc;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.capgemini.jdbc.EmployeePayrollServiceJDBCException.ExceptionType;

public class EmployeePayrollServiceJDBC {
	private static final Logger log = LogManager.getLogger(EmployeePayrollServiceJDBC.class);

	public static void main(String[] args) throws EmployeePayrollServiceJDBCException {
		EmployeePayrollServiceJDBC employeePayrollService = new EmployeePayrollServiceJDBC();
		employeePayrollService.establishConnection();
	}

	/**
	 * UC1 Connection to Database
	 * 
	 * @throws EmployeePayrollServiceJDBCException
	 */
	private void establishConnection() throws EmployeePayrollServiceJDBCException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String user = "root";
		String password = "Shashi@123";
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			log.info("Driver found!");
		} catch (ClassNotFoundException e) {
			throw new EmployeePayrollServiceJDBCException("Cannot find the JDBC Driver!!",
					ExceptionType.CANNOT_LOAD_DRIVER);
		}
		listDrivers();
		try {
			System.out.println("\nConnecting to database: " + jdbcURL);
			connection = DriverManager.getConnection(jdbcURL, user, password);
			System.out.println("Connection estabilshed with: " + connection);
		} catch (SQLException e) {
			throw new EmployeePayrollServiceJDBCException("Cannot connect to the JDBC Driver!!",
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
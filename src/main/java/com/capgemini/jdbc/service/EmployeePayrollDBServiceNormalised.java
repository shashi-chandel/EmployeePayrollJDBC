package com.capgemini.jdbc.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.capgemini.jdbc.EmployeePayrollData;

public class EmployeePayrollDBServiceNormalised {
	private static EmployeePayrollDBServiceNormalised employeePayrollDBServiceNormalised;
	private PreparedStatement employeePayrollDataStatementNormalised;

	private EmployeePayrollDBServiceNormalised() {

	}

	public static EmployeePayrollDBServiceNormalised getInstance() {
		if (employeePayrollDBServiceNormalised == null)
			employeePayrollDBServiceNormalised = new EmployeePayrollDBServiceNormalised();
		return employeePayrollDBServiceNormalised;
	}

	public List<EmployeePayrollData> readData() {
		String sql ="SELECT e.id, e.employee_name, e.start, e.gender, e.comp_id, c.comp_name, p.dept_name, p.basic_pay from employee e left join  payroll p ON p.emp_id = e.id left join company c ON c.comp_id = e.comp_id;";
		return this.getEmployeePayrollDataUsingSQLQuery(sql);
	}

	private List<EmployeePayrollData> getEmployeePayrollDataUsingSQLQuery(String sql) {
		List<EmployeePayrollData> employeePayrollList = null;
		try (Connection connection = this.getConnection();) {
			System.out.println("Connection");
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("payroll");
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollData(ResultSet resultSet) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		List<String> department = new ArrayList<String>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int companyId = resultSet.getInt("comp_id");
				String name = resultSet.getString("employee_name");
				String gender = resultSet.getString("gender");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				String companyName = resultSet.getString("comp_name");
				String dept = resultSet.getString("dept_name");
				double salary = resultSet.getDouble("basic_pay");
				System.out.println(dept);
				department.add(dept);
				System.out.println(id);
				String[] departmentArray = new String[department.size()];
				employeePayrollList.add(new EmployeePayrollData(id, name, gender, salary, startDate, companyName,
						companyId, department.toArray(departmentArray)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("return");
		return employeePayrollList;
	}

	public List<EmployeePayrollData> getEmployeePayrollData(String name) {
		List<EmployeePayrollData> employeePayrollList = null;
		if (this.employeePayrollDataStatementNormalised == null)
			this.preparedStatementForEmployeeData();
		try {
			employeePayrollDataStatementNormalised.setString(1, name);
			ResultSet resultSet = employeePayrollDataStatementNormalised.executeQuery();
			employeePayrollList = this.getEmployeePayrollData(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private void preparedStatementForEmployeeData() {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT e.id, e.employee_name, e.gender, e.comp_id, c.comp_name p.basic_pay from employee e\r\n"
					+ "left join  payroll p ON p.emp_id = e.id\r\n"
					+ "left join company c ON c.comp_id = e.comp_id;";

			employeePayrollDataStatementNormalised = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Connection getConnection() throws SQLException {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String user = "root";
		String password = "Shashi@123";
		Connection connection;
		System.out.println("\nConnecting to database: " + jdbcURL);
		connection = DriverManager.getConnection(jdbcURL, user, password);
		System.out.println("Connection estabilshed with: " + connection);
		return connection;
	}

}

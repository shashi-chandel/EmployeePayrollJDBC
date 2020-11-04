package com.capgemini.jdbc.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		String sql = "SELECT e.id, e.employee_name, e.start, e.gender, e.comp_id, c.comp_name, p.dept_name, p.basic_pay from employee e left join  payroll p ON p.emp_id = e.id left join company c ON c.comp_id = e.comp_id;";
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
			String sql = "SELECT e.id, e.employee_name, e.gender, e.comp_id, c.comp_name,e.start,dept_name, p.basic_pay from employee e left join payroll p ON p.emp_id = e.id left join company c ON c.comp_id = e.comp_id where e.employee_name=?;";
			
			employeePayrollDataStatementNormalised = connection.prepareStatement(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<EmployeePayrollData> getEmployeeForDateRange(LocalDate startDate, LocalDate endDate) {
		String sql = String.format(
				"SELECT e.id, e.employee_name, e.gender, e.comp_id, c.comp_name, e.start, p.basic_pay FROM employee e left join payroll p ON p.emp_id = e.id left join company c ON c.comp_id = e.comp_id WHERE e.start BETWEEN CAST('2011-01-01' AS DATE) AND DATE(NOW());",
				Date.valueOf(startDate), Date.valueOf(endDate));
		return this.getEmployeePayrollDataUsingDB(sql);
	}

	private List<EmployeePayrollData> getEmployeePayrollDataNormalised(ResultSet resultSet) {
		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
		try {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				int companyId = resultSet.getInt("comp_id");
				String name = resultSet.getString("employee_name");
				String gender = resultSet.getString("gender");
				LocalDate startDate = resultSet.getDate("start").toLocalDate();
				String companyName = resultSet.getString("comp_name");
				double salary = resultSet.getDouble("basic_pay");
				employeePayrollList
						.add(new EmployeePayrollData(id, name, gender, salary, startDate, companyName, companyId));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	private List<EmployeePayrollData> getEmployeePayrollDataUsingDB(String sql) {
		ResultSet resultSet;
		List<EmployeePayrollData> employeePayrollList = null;
		try (Connection connection = this.getConnection();) {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			resultSet = prepareStatement.executeQuery(sql);
			employeePayrollList = this.getEmployeePayrollDataNormalised(resultSet);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return employeePayrollList;
	}

	public Map<String, Double> getAverageSalaryByGender() {
		String sql = "SELECT e.gender,AVG(p.basic_pay) as avg_basic_pay FROM employee e left join payroll p on p.emp_id = e.id GROUP BY e.gender;";
		Map<String, Double> genderToAverageSalaryMap = new HashMap<>();
		try (Connection connection = this.getConnection();) {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			ResultSet resultSet = prepareStatement.executeQuery(sql);
			while (resultSet.next()) {
				String gender = resultSet.getString("gender");
				double salary = resultSet.getDouble("avg_basic_pay");
				genderToAverageSalaryMap.put(gender, salary);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return genderToAverageSalaryMap;
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

	public int updateEmployeeData(String name, Double salary) {
		return this.updateEmployeeDataUsingPreparedStatement(name, salary);
	}

	private int updateEmployeeDataUsingPreparedStatement(String name, double salary) {
		String sql = String.format("UPDATE payroll SET basic_pay = %.2f WHERE emp_id = "
				+ "(SELECT id from employee WHERE employee_name = '%s');", salary, name);
		try (Connection connection = this.getConnection();) {
			PreparedStatement prepareStatement = connection.prepareStatement(sql);
			return prepareStatement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}

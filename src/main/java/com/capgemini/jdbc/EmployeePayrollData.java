package com.capgemini.jdbc;

import java.time.LocalDate;

public class EmployeePayrollData {
	private int id;
	public String name;
	public double salary;
	private LocalDate startDate;
	public String gender;
	public String companyName;
	public int companyId;
	public String department[];
	public int departmentId;
	public String departmentName;
	public boolean is_active;

	public EmployeePayrollData(int id, String name, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	public EmployeePayrollData(int departmentId, String departmentName) {
		this.departmentId = departmentId;
		this.departmentName = departmentName;

	}

	public EmployeePayrollData(int id, int departmentId) {
		this.departmentId = departmentId;
		this.id = id;

	}

	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
		this(id, name, salary);
		this.startDate = startDate;
	}

	public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate) {
		this(id, name, salary, startDate);
		this.gender = gender;
	}

	public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate,
			String companyName, int companyId, String department[]) {
		this(id, name, gender, salary, startDate);
		this.companyName = companyName;
		this.companyId = companyId;
		this.department = department;
	}

	public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate,
			String companyName, int companyId) {
		this(id, name, gender, salary, startDate);
		this.companyName = companyName;
		this.companyId = companyId;
	}

	public EmployeePayrollData(int id, String name, String gender, int companyId, String companyName, double salary,
			LocalDate startDate) {
		this.id = id;
		this.name = name;
		this.gender = gender;
		this.companyId = companyId;
		this.companyName = companyName;
		this.salary = salary;
		this.startDate = startDate;
	}

	public EmployeePayrollData(int id, String name, String gender, double salary, LocalDate startDate,
			String companyName, int companyId, boolean is_active) {
		this(id, name, gender, salary, startDate, companyName, companyId);
		this.is_active = is_active;
	}

	public String[] getDepartment() {
		return department;
	}

	public void setDepartment(String[] department) {
		this.department = department;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "EmployeePayrollData [id=" + id + ", name=" + name + ", salary=" + salary + ", startDate=" + startDate
				+ ", gender=" + gender + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EmployeePayrollData other = (EmployeePayrollData) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(salary) != Double.doubleToLongBits(other.salary))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

	public void printDepartments() {
		String departments[] = this.getDepartment();
		for (String s : departments) {
			System.out.println("id: " + this.getId() + ":" + s);
		}
	}
}

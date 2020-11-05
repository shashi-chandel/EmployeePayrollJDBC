package com.capgemini.jdbc;

import java.time.LocalDate;
import java.util.Arrays;

public class EmployeePayrollData {
	private int id;
	public String name;
	public double salary;
	public LocalDate startDate;
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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + companyId;
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + Arrays.hashCode(department);
		result = prime * result + departmentId;
		result = prime * result + ((departmentName == null) ? 0 : departmentName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + id;
		result = prime * result + (is_active ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(salary);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
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
		if (companyId != other.companyId)
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (!Arrays.equals(department, other.department))
			return false;
		if (departmentId != other.departmentId)
			return false;
		if (departmentName == null) {
			if (other.departmentName != null)
				return false;
		} else if (!departmentName.equals(other.departmentName))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id != other.id)
			return false;
		if (is_active != other.is_active)
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

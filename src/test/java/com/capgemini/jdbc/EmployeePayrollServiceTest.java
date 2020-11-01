package com.capgemini.jdbc;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.capgemini.jdbc.service.EmployeePayrollService;
import com.capgemini.jdbc.service.EmployeePayrollService.IOService;

public class EmployeePayrollServiceTest {

	@Test
	public void countEntries() {
		EmployeePayrollData[] arrayOfEmployee = { new EmployeePayrollData(1, "Shashi", 100000.0),
				new EmployeePayrollData(2, "Aditya", 200000.0), new EmployeePayrollData(3, "Shivam", 300000.0) };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployee));
		employeePayrollService.writeEmployeePayrollData(IOService.FILE_IO);
		employeePayrollService.printData(IOService.FILE_IO);
		long entries = employeePayrollService.countEntries(IOService.FILE_IO);
		Assert.assertEquals(3, entries);
	}

	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readPayrollData(IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}
}

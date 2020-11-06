package com.capgemini.jdbc;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.capgemini.jdbc.service.EmployeePayrollService;
import com.capgemini.jdbc.service.EmployeePayrollService.IOService;
import com.google.gson.Gson;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

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
	public void givenEmployeePayrollData_ShouldMatchAverageSalary_GroupByGender() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		Map<String, Double> employeePayrollData = employeePayrollService.readPayrollDataForAvgSalary(IOService.DB_IO);
		Assert.assertEquals(2, employeePayrollData.size());
	}

	@Test
	public void givenEmployeePayrollInNormalisedDB_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readPayrollData(IOService.DB_IO);
		Assert.assertEquals(3, employeePayrollData.size());
	}

	@Test
	public void givenDateRangeForEmployeeInNormalised_WhenRetrieved_ShouldMatchEmployeeCount() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readPayrollData(IOService.DB_IO);
		LocalDate startDate = LocalDate.of(2001, 01, 01);
		LocalDate endDate = LocalDate.now();
		List<EmployeePayrollData> employeePayrollData = employeePayrollService.readPayrollDataForRange(IOService.DB_IO,
				startDate, endDate);
		Assert.assertEquals(2, employeePayrollData.size());
	}

	@Test
	public void givenEmployees_WhenAddedToDB_ShouldMatchEmployeeEntries() {
		EmployeePayrollData[] arrayOfEmployee = { new EmployeePayrollData(0, "Jeff", "M", 100000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Bill", "M", 200000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Mahesh", "M", 400000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Mukesh", "M", 300000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Sunder", "M", 500000.0, LocalDate.now()),
				new EmployeePayrollData(0, "Anil", "M", 100000.0, LocalDate.now()) };
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readPayrollData(IOService.DB_IO);
		Instant start = Instant.now();
		employeePayrollService.addEmployeeToPayroll(Arrays.asList(arrayOfEmployee));
		Instant end = Instant.now();
		System.out.println("Duration without thread : " + Duration.between(start, end));
		Instant threadStart = Instant.now();
		employeePayrollService.addEmployeeToPayrollWithThreads(Arrays.asList(arrayOfEmployee));
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread : " + Duration.between(threadStart, threadEnd));
		Assert.assertEquals(15, employeePayrollService.countEntries(IOService.DB_IO));  
	}

	@Test
	public void givenNewSalariesForMultipleEmployee_WhenUpdated_ShouldSyncWithDB() {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		employeePayrollService.readPayrollData(IOService.DB_IO);
		Map<String, Double> employeeSalaryMap = new HashMap<>();
		Instant threadStart = Instant.now();
		employeeSalaryMap.put("Terisa", 1500000.00);
		employeeSalaryMap.put("Bill", 50000.00);
		employeeSalaryMap.put("Charlie", 60000.00);
		employeePayrollService.updateMultipleEmployeesSalary(employeeSalaryMap);
		Instant threadEnd = Instant.now();
		System.out.println("Duration with Thread : " + Duration.between(threadStart, threadEnd));
		boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Bill");
		Assert.assertTrue(result);
	}
	
	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}
	
	public EmployeePayrollData[] getEmployeeList() {
		Response response = RestAssured.get("/employees");
		System.out.println("Employee payroll entries in JSON Server :\n" + response.asString());
		EmployeePayrollData[] arrayOfEmployees = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return arrayOfEmployees;
	}

	public Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}

	@Test
	public void givenNewEmployee_WhenAdded_ShouldMatch() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		EmployeePayrollData employeePayrollData = null;
		employeePayrollData = new EmployeePayrollData(0, "Manish", "M", 3000000.00, LocalDate.now());
		Response response = addEmployeeToJsonServer(employeePayrollData);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);

		employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
		employeePayrollService.addEmployeePayroll(employeePayrollData, IOService.REST_IO);
		long entries = employeePayrollService.countEntries(IOService.REST_IO);
		Assert.assertEquals(7, entries);
	}
	
	@Test
	public void givenMultipleEmployees_WhenAdded_ShouldMatch() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		EmployeePayrollData[] arrayOfEmployeePayrolls = {
				new EmployeePayrollData(0, "Mukesh", "M", 4000000.00, LocalDate.now()),
				new EmployeePayrollData(0, "Anil", "M", 3000000.00, LocalDate.now()),
				new EmployeePayrollData(0, "Neeta", "F", 2000000.00, LocalDate.now()) };
		for (EmployeePayrollData employeePayrollData : arrayOfEmployeePayrolls) {
			Response response = addEmployeeToJsonServer(employeePayrollData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
			employeePayrollService.addEmployeePayroll(employeePayrollData, IOService.REST_IO);
		}
		long entries = employeePayrollService.countEntries(IOService.REST_IO);
		Assert.assertEquals(6, entries);
	}
}

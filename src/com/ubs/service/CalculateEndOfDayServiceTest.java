package com.ubs.service;

import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import com.ubs.test.service.EndOfDayPositionServiceImplTest;

public class CalculateEndOfDayServiceTest {

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(Computer.serial(),EndOfDayPositionServiceImplTest.class);
		printResult(result);
		
	}

	public static void printResult(Result result) {
		System.out.printf("Summary - Test ran: %s, Failed: %s%n",
				result.getRunCount(), result.getFailureCount());
	}
}

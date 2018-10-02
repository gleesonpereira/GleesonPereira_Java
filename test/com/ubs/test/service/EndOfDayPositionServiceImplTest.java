package com.ubs.test.service;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

import com.ubs.exception.EndOfDayPositionServiceException;
import com.ubs.model.EndOfDayPosition;
import com.ubs.model.StartOfDayPosition;
import com.ubs.model.Transaction;
import com.ubs.service.EndOfDayPositionServiceImpl;

public class EndOfDayPositionServiceImplTest {
	
	@Rule 
	public TestName name = new TestName();
	
	private static String inputPositionFile;
	private static String inputInvalidPositionFile;
	private static String inputTransactionFile;
	private static String outputFilePath;
	
	@BeforeClass
	public static void setup() {
		inputPositionFile = "H:\\Workspace_Oxygen\\UBSCodingAssignment\\resources\\InputPosition.txt";
		inputInvalidPositionFile = "H:\\Workspace_Oxygen\\UBSCodingAssignment\\resources\\InputPositionInvalid.txt";
		inputTransactionFile = "H:\\Workspace_Oxygen\\UBSCodingAssignment\\resources\\Input_Transactions.txt";
		outputFilePath = "H:\\Workspace_Oxygen\\UBSCodingAssignment\\resources";
	}
	
	@Test(expected=EndOfDayPositionServiceException.class)
	public void testReadFileAndCreatePositionWithNull() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.readInputPositionFileAndCreatePosition(null);
	}
	
	@Test(expected=EndOfDayPositionServiceException.class)
	public void testReadFileAndCreatePositionWithEmpty() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.readInputPositionFileAndCreatePosition("");
	}
	
	@Test
	public void testReadFileAndCreatePositionWithValidFile() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		List<StartOfDayPosition> expectedStartOfDayPositionList = new ArrayList<>();
		expectedStartOfDayPositionList.add(new StartOfDayPosition("IBM", new Long("101").longValue(), 'E', new Long("100000").longValue()));
		expectedStartOfDayPositionList.add(new StartOfDayPosition("IBM", new Long("201").longValue(), 'I', new Long("-100000").longValue()));
		assertThat(service.readInputPositionFileAndCreatePosition(inputPositionFile), equalTo(expectedStartOfDayPositionList));
	}
	
	@Test(expected=EndOfDayPositionServiceException.class)
	public void testReadFileAndCreatePositionWithInvalidFile() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.readInputPositionFileAndCreatePosition(inputInvalidPositionFile);
	}
	
	@Test
	public void testReadFileAndCreateTransactionWithNull() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		assertThat(service.readInputTransactionFileAndCreateTransaction(null), equalTo(null));
	}
	
	@Test
	public void testReadFileAndCreateTransactionWithEmpty() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		assertThat(service.readInputTransactionFileAndCreateTransaction(""), equalTo(null));
	}
	
	@Test
	public void testReadFileAndCreateTransactionWithValidFile() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		List<Transaction> expectedTransactionList = new ArrayList<>();
		expectedTransactionList.add(new Transaction(1L, "IBM", 'B', 1000));
		assertThat(service.readInputTransactionFileAndCreateTransaction(inputTransactionFile), equalTo(expectedTransactionList));
	}
	
	@Test(expected=EndOfDayPositionServiceException.class)
	public void testCalculateEndOfDayWithNull() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.calculateEndOfDayForInstrument(null, null);
	}
	
	@Test(expected=EndOfDayPositionServiceException.class)
	public void testCalculateEndOfDayWithEmpty() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.calculateEndOfDayForInstrument("", "");
	}
	
	@Test
	public void testCalculateEndOfDayWithNonEmptyInputPathAndNullTransactionPath() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		List<EndOfDayPosition> expectedEndOfDayPositionList = new ArrayList<>();
		EndOfDayPosition position1 = new EndOfDayPosition("IBM",new Long("101").longValue(),'E',new Long("100000").longValue(), new Long("0").longValue());
		EndOfDayPosition position2 = new EndOfDayPosition("IBM",new Long("201").longValue(),'I',new Long("-100000").longValue(), new Long("0").longValue());
		expectedEndOfDayPositionList.add(position1);
		expectedEndOfDayPositionList.add(position2);
		assertThat(service.calculateEndOfDayForInstrument(inputPositionFile, null), equalTo(expectedEndOfDayPositionList));
	}
	
	@Test
	public void testCalculateEndOfDayWithNonEmptyInputPathAndEmptyTransactionPath() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		List<EndOfDayPosition> expectedEndOfDayPositionList = new ArrayList<>();
		EndOfDayPosition position1 = new EndOfDayPosition("IBM",new Long("101").longValue(),'E',new Long("100000").longValue(), new Long("0").longValue());
		EndOfDayPosition position2 = new EndOfDayPosition("IBM",new Long("201").longValue(),'I',new Long("-100000").longValue(), new Long("0").longValue());
		expectedEndOfDayPositionList.add(position1);
		expectedEndOfDayPositionList.add(position2);
		assertThat(service.calculateEndOfDayForInstrument(inputPositionFile, ""), equalTo(expectedEndOfDayPositionList));
	}
	
	@Test
	public void testCalculateEndOfDayWithNonEmptyInputPathAndTransactionPath() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		List<EndOfDayPosition> expectedEndOfDayPositionList = new ArrayList<>();
		EndOfDayPosition position1 = new EndOfDayPosition("IBM",new Long("101").longValue(),'E',new Long("101000").longValue(), new Long("1000").longValue());
		EndOfDayPosition position2 = new EndOfDayPosition("IBM",new Long("201").longValue(),'I',new Long("-101000").longValue(), new Long("-1000").longValue());
		expectedEndOfDayPositionList.add(position1);
		expectedEndOfDayPositionList.add(position2);
		assertThat(service.calculateEndOfDayForInstrument(inputPositionFile, inputTransactionFile), equalTo(expectedEndOfDayPositionList));
	}
	
	@Test(expected=EndOfDayPositionServiceException.class)
	public void testWriteEndOfDayPositionFileWithNoOuputPath() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.writeEndOfDayPositionFile(service.calculateEndOfDayForInstrument(inputPositionFile, null),null);
	}
	
	@Test(expected=EndOfDayPositionServiceException.class)
	public void testWriteEndOfDayPositionFileWithNoEndOfDayPositions() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		service.writeEndOfDayPositionFile(null, outputFilePath);
	}
	
	@Test
	public void testWriteEndOfDayPositionFileWithValidParameters() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		assertTrue(service.writeEndOfDayPositionFile(service.calculateEndOfDayForInstrument(inputPositionFile, inputTransactionFile), outputFilePath));
	}
	
	@Test
	public void testWriteEndOfDayPositionFileWithNoTransactionFile() {
		printThreadInfo(name.getMethodName());
		EndOfDayPositionServiceImpl service = new EndOfDayPositionServiceImpl();
		assertTrue(service.writeEndOfDayPositionFile(service.calculateEndOfDayForInstrument(inputPositionFile, null), outputFilePath));
	}
	
	private static void printThreadInfo(String testName) {
	      System.out.printf("Test= %s, Thread= %s, Time= %s%n",
	              testName, Thread.currentThread().getName(), LocalTime.now());
	      try {
	          TimeUnit.SECONDS.sleep(1);
	      } catch (InterruptedException e) {
	          e.printStackTrace();
	      }
	  }
}
